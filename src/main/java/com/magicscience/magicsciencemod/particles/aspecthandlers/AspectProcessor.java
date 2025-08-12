package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleBlockHitPacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleEffectsPacket;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.AttributeEntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.BaseEntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.EntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.IEntityFilter;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class AspectProcessor {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull MagicParticle particle;
    private final @NotNull SpellData spellData;

    private final @NotNull IEntityFilter entityFilter;
    private final int damage;

    public AspectProcessor(@NotNull MagicParticle particle) {
        this.particle = particle;
        this.spellData = particle.getSpellData();

        this.entityFilter = new EntityFilter(
            new BaseEntityFilter(),
            new AttributeEntityFilter(spellData)
        );

        var coreFactory = new MagicCoreFactory();
        this.damage = coreFactory.createById(
            spellData.coreId(),
            spellData.coreStack()
        ).getDamage();
    }

    public void process() {
        // ToDo:
        // Collision with block

        // сначала — проверка коллизий с блоками (ray-trace от центра AABB по вектору направления)
        AABB boundingBox = particle.getBoundingBox();

        Vec3 start = new Vec3(
            (boundingBox.minX + boundingBox.maxX) / 2.0,
            (boundingBox.minY + boundingBox.maxY) / 2.0,
            (boundingBox.minZ + boundingBox.maxZ) / 2.0
        );
        Vec3 end = start.add(particle.getDirectionPos()); // xd,yd,zd через getter в MagicParticle

        var blockHitResult = particle.getLevel().clip(
            new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.ANY,
                null
            )
        );

        handleBlockCollision(blockHitResult);

        particle.getLevel()
            .getEntities((Entity) null, boundingBox, entityFilter)
            .forEach(this::handleEntityCollision);
    }

    private void handleEntityCollision(Entity entity) {
        // Send effects
        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundParticleEffectsPacket(
                entity.getId(),
                spellData.coreId()
            )
        );

        // Send damage
        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundParticleDamagePacket(
                entity.getId(),
                damage,
                spellData.ownerId()
            )
        );

        particle.remove();
    }

    private void handleBlockCollision(BlockHitResult blockHitResult) {
        var blockPos = blockHitResult.getBlockPos();
        
        var blockState = particle.getLevel().getBlockState(blockPos);

        if (!blockState.isAir()) {
            Vec3 hitVec = blockHitResult.getLocation();
            int face = blockHitResult.getDirection().get3DDataValue();

            CompoundTag extra = new CompoundTag();
            extra.putInt("coreId", spellData.coreId());

            ModMessagesMagicParticles.CHANNEL.sendToServer(
                new ServerboundParticleBlockHitPacket(
                    blockPos,
                    face,
                    hitVec,
                    extra
                )
            );
        }
    }
}

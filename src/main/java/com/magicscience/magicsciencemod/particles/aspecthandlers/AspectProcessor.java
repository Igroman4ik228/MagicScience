package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleBlockHitPacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleEffectsPacket;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.block.ConfigBlockFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.block.CoreBlockFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.AttributeEntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.ConfigEntityFilter;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Predicate;

public class AspectProcessor {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final MagicCoreFactory CORE_FACTORY = new MagicCoreFactory();

    private final @NotNull MagicParticle particle;
    private final @NotNull SpellData spellData;
    private final @NotNull ClientLevel level;

    private final @NotNull Predicate<Entity> entityFilter;
    private final @NotNull Predicate<BlockState> blockFilter;

    private final @NotNull CompoundTag additionalArgs = new CompoundTag();

    private final int damage;

    public AspectProcessor(@NotNull MagicParticle particle) {
        this.particle = particle;
        this.spellData = particle.getSpellData();
        this.level = particle.getLevel();

        this.entityFilter = new ConfigEntityFilter()
            .and(new AttributeEntityFilter(spellData.attributeIds()));

        this.blockFilter = new ConfigBlockFilter()
            .and(new CoreBlockFilter(spellData.coreId()));

        this.damage = CORE_FACTORY.createById(
            spellData.coreId(),
            spellData.coreStack()
        ).getDamage();

        // ToDo: Сделать динамическое заполнение additionalArgs в зависимости от SpellData
        additionalArgs.putInt("coreId", spellData.coreId());
    }

    public void process() {
        AABB boundingBox = particle.getBoundingBox();

        // Center
        Vec3 start = boundingBox.getCenter();
        Vec3 end = start.add(particle.getDirectionPos());

        var blockHitResult = level.clip(
            new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.ANY,
                null
            )
        );

        handleBlockCollision(blockHitResult);

        // Particle remove -> just first entity
        level.getEntities((Entity) null, boundingBox, entityFilter)
            .stream()
            .findFirst()
            .ifPresent(this::handleEntityCollision);
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
        var blockState = level.getBlockState(blockPos);

        if (blockHitResult.getType()!=HitResult.Type.BLOCK) return;
        if (blockState.isAir()) return;
        if (blockFilter.test(blockState)) return;

        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundParticleBlockHitPacket(
                blockHitResult,
                additionalArgs
            )
        );
    }
}

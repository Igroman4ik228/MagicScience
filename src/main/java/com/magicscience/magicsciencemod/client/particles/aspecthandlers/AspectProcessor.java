package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block.ConfigBlockFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block.CoreBlockFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity.AttributeEntityFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity.ConfigEntityFilter;
import com.magicscience.magicsciencemod.network.magicparticles.ServerParticleBlockHitPacket;
import com.magicscience.magicsciencemod.network.magicparticles.ServerParticleDamagePacket;
import com.magicscience.magicsciencemod.network.magicparticles.ServerParticleEffectsPacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import com.magicscience.magicsciencemod.util.MathHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
        processBlockCollision();

        processEntityCollision();
    }

    private void processBlockCollision() {
        Vec3 center = particle.getBoundingBox().getCenter();
        Vec3 direction = particle.getDirectionPos();

        // No speed
        if (direction.lengthSqr()==0) {
            BlockPos pos = BlockPos.containing(center);

            handleBlockCollision(
                new BlockHitResult(
                    center,
                    MathHelper.getClosestDirection(pos, center),
                    pos,
                    true
                )
            );
            return;
        }

        rayTraceBlock(center, direction);
    }

    private void processEntityCollision() {
        // Particle remove -> just first entity
        level.getEntities((Entity) null, particle.getBoundingBox(), entityFilter)
            .stream()
            .findFirst()
            .ifPresent(this::handleEntityCollision);
    }

    private void rayTraceBlock(Vec3 start, Vec3 direction) {
        Vec3 end = start.add(direction);

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
    }


    private void handleBlockCollision(BlockHitResult blockHitResult) {
        var blockPos = blockHitResult.getBlockPos();
        var blockState = level.getBlockState(blockPos);

        if (blockHitResult.getType()!=HitResult.Type.BLOCK) return;
        if (blockState.isAir()) return;
        if (blockFilter.test(blockState)) return;

        if (spellData.coreId()==CoreTypes.FIRE.getId()) {
            if (blockState.getBlock()==Blocks.WATER) {
                particle.remove();
                return;
            }
        }

        ModNetwork.CHANNEL.sendToServer(
            new ServerParticleBlockHitPacket(
                blockHitResult,
                particle.getParticleUUID(),
                additionalArgs
            )
        );
    }

    private void handleEntityCollision(Entity entity) {
        // Send effects
        ModNetwork.CHANNEL.sendToServer(
            new ServerParticleEffectsPacket(
                entity.getId(),
                spellData.coreId()
            )
        );

        // Send damage
        ModNetwork.CHANNEL.sendToServer(
            new ServerParticleDamagePacket(
                entity.getId(),
                damage,
                spellData.ownerId()
            )
        );

        particle.remove();
    }
}

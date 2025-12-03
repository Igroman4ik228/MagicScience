package com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers;

import com.magicscience.magicsciencemod.aspects.cores.CoreType;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.network.magicparticles.ServerParticleBlockHitPacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import com.magicscience.magicsciencemod.util.BlockMathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class BlockCollisionHandler extends BaseCollisionHandler<BlockState> {
    public BlockCollisionHandler(
        @NotNull MagicParticle particle,
        @NotNull SpellData spellData,
        @NotNull Predicate<BlockState> blockFilter,
        @NotNull ClientLevel level
    ) {
        super(particle, spellData, blockFilter, level);
    }

    public void handleCollision() {
        Vec3 center = particle.getBoundingBox().getCenter();
        Vec3 direction = particle.getDirectionVec();

        // No speed
        if (direction.lengthSqr()==0) {
            handleStaticPosition(center);
            return;
        }

        rayTraceBlock(center, direction);
    }

    private void handleStaticPosition(@NotNull Vec3 center) {
        // Static and inside in block
        BlockPos pos = BlockPos.containing(center);
        handleBlockCollision(
            new BlockHitResult(
                center,
                BlockMathUtil.getClosestDirection(pos, center),
                pos,
                true
            )
        );
    }

    private void rayTraceBlock(@NotNull Vec3 start, Vec3 direction) {
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

    private void handleBlockCollision(@NotNull BlockHitResult blockHitResult) {
        if (blockHitResult.getType()!=HitResult.Type.BLOCK) return;

        var blockPos = blockHitResult.getBlockPos();
        var blockState = level.getBlockState(blockPos);

        if (blockState.isAir()) return;
        //if (filter.test(blockState)) return;

        if (spellData.coreId()==CoreType.FIRE.getId()) {
            if (blockState.getBlock()==Blocks.WATER) {
                particle.remove();
                return;
            }
        }

        ModNetwork.CHANNEL.sendToServer(
            new ServerParticleBlockHitPacket(
                blockHitResult,
                particle.getParticleUUID(),
                spellData.coreId()
            )
        );
    }
}

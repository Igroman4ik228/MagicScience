package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

public class ServerParticleBlockHitPacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull BlockHitResult blockHitResult;
    private final @NotNull CompoundTag additionalArgs;

    public ServerParticleBlockHitPacket(
        @NotNull BlockHitResult blockHitResult,
        @NotNull CompoundTag additionalArgs
    ) {
        this.blockHitResult = blockHitResult;
        this.additionalArgs = additionalArgs;
    }

    public ServerParticleBlockHitPacket(FriendlyByteBuf buf) {
        this.blockHitResult = buf.readBlockHitResult();
        this.additionalArgs = Objects.requireNonNull(buf.readNbt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockHitResult(blockHitResult);
        buf.writeNbt(additionalArgs);
    }

    @Override
    public void handle(ServerPlayer player) {
        var level = player.serverLevel();
        var blockPos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(blockPos);

        if (blockHitResult.getType()!=HitResult.Type.BLOCK) {
            LOGGER.debug("Ignoring non-block hit or null result from {}", player.getName().getString());
            return;
        }

        int coreId = additionalArgs.contains("coreId") ? additionalArgs.getInt("coreId"):-1;

        LOGGER.info("Block hit at {} state {} coreId {}", blockPos, state, coreId);
        if (coreId==CoreTypes.FIRE.getId()) {
            if (state.getBlock()==Blocks.TNT) {
                level.removeBlock(blockPos, false);

                var centerBlockPos = blockPos.getCenter();
                PrimedTnt primed = new PrimedTnt(
                    level,
                    centerBlockPos.x,
                    centerBlockPos.y,
                    centerBlockPos.z,
                    player
                );
                level.addFreshEntity(primed);
                LOGGER.info("Ignited TNT at {}", blockPos);
                return;
            }

            // todo: particle remove
            if (state.isFlammable(level, blockPos, blockHitResult.getDirection())) {
                var abovePos = blockPos.relative(blockHitResult.getDirection());

                if (level.getBlockState(abovePos).isAir()) {
                    level.setBlockAndUpdate(abovePos, Blocks.FIRE.defaultBlockState());
                    LOGGER.info("Ignited block at {} with fire on {}", abovePos, blockPos);
                }
            }
        }
    }
}
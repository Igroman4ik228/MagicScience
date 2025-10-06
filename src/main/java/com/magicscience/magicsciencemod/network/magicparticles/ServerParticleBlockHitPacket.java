package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class ServerParticleBlockHitPacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull BlockHitResult blockHitResult;
    private final @NotNull UUID particleUUID;
    private final int coreId;

    public ServerParticleBlockHitPacket(
        @NotNull BlockHitResult blockHitResult,
        @NotNull UUID particleUUID,
        int coreId
    ) {
        this.blockHitResult = blockHitResult;
        this.particleUUID = particleUUID;
        this.coreId = coreId;
    }

    public ServerParticleBlockHitPacket(FriendlyByteBuf buf) {
        this.blockHitResult = buf.readBlockHitResult();
        this.particleUUID = buf.readUUID();
        this.coreId = buf.readInt();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeBlockHitResult(blockHitResult);
        buf.writeUUID(particleUUID);
        buf.writeInt(coreId);
    }

    @Override
    public void handle(@NotNull ServerPlayer player) {
//        LOGGER.info("ServerParticleBlockHitPacket name player: {}", player.getName());

        var level = player.serverLevel();
        var blockPos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(blockPos);

        if (blockHitResult.getType()!=HitResult.Type.BLOCK) {
            LOGGER.debug("Ignoring non-block hit or null result from {}", player.getName().getString());
            return;
        }

        LOGGER.info("Block hit at {} state {} coreId {}", blockPos, state, coreId);

        var core = CoreTypeHelper.findInstance(coreId);
        core.processingBlock(blockHitResult, player);
    }
}
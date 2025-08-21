package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Supplier;

public class ServerboundParticleBlockHitPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull BlockHitResult blockHitResult;
    private final @NotNull CompoundTag additionalArgs;

    public ServerboundParticleBlockHitPacket(@NotNull BlockHitResult blockHitResult, @NotNull CompoundTag additionalArgs) {
        this.blockHitResult = blockHitResult;
        this.additionalArgs = additionalArgs;
    }

    public ServerboundParticleBlockHitPacket(FriendlyByteBuf buf) {
        this.blockHitResult = buf.readBlockHitResult();
        this.additionalArgs = Objects.requireNonNull(buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockHitResult(blockHitResult);
        buf.writeNbt(additionalArgs);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            LOGGER.info("ServerboundParticleBlockHitPacket start");

            ServerPlayer sender = ctx.getSender();
            if (sender==null) return;

            var level = sender.level();
            var blockPos = blockHitResult.getBlockPos();
            BlockState state = level.getBlockState(blockPos);

            if (blockHitResult.getType()!=HitResult.Type.BLOCK) {
                LOGGER.debug("Ignoring non-block hit or null result from {}", sender.getName().getString());
                return;
            }

            int coreId = additionalArgs.contains("coreId") ? additionalArgs.getInt("coreId"):-1;

            LOGGER.info("Block hit at {} state {} coreId {}", blockPos, state, coreId);

            var core = CoreTypeHelper.findInstance(coreId);
            core.processingBlock(state.getBlock(), level, blockHitResult, sender);
        });
        ctx.setPacketHandled(true);
    }
}
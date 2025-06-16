package com.magicscience.magicsciencemod.net;

import com.magicscience.magicsciencemod.blocks.LightBlockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerboundPlaceLightPacket {
    private final UUID particleUUID;
    private final BlockPos pos;

    public ServerboundPlaceLightPacket(UUID particleUUID, BlockPos pos) {
        this.particleUUID = particleUUID;
        this.pos = pos;
    }

    public ServerboundPlaceLightPacket(FriendlyByteBuf buf) {
        this.particleUUID = buf.readUUID();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(particleUUID);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            LightBlockManager.placeLightBlock((ServerLevel)player.level(), particleUUID, pos);
        });
        ctx.get().setPacketHandled(true);
    }
}
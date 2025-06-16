package com.magicscience.magicsciencemod.net;

import com.magicscience.magicsciencemod.blocks.LightBlockManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerboundRemoveLightPacket {
    private final UUID particleUUID;

    public ServerboundRemoveLightPacket(UUID particleUUID) {
        this.particleUUID = particleUUID;
    }

    public ServerboundRemoveLightPacket(FriendlyByteBuf buf) {
        this.particleUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(particleUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            LightBlockManager.removeLightBlock((ServerLevel)player.level(), particleUUID);
        });
        ctx.get().setPacketHandled(true);
    }
}
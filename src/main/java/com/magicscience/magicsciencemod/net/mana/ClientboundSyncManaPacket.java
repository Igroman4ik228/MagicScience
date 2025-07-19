package com.magicscience.magicsciencemod.net.mana;

import com.magicscience.magicsciencemod.registry.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSyncManaPacket {
    private final int mana;

    public ClientboundSyncManaPacket(int mana) {
        this.mana = mana;
    }

    public ClientboundSyncManaPacket(FriendlyByteBuf buf) {
        this.mana = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(mana);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player == null) return;

            player.getCapability(ModCapabilities.MANA_CAPABILITY).ifPresent(cap -> {
                cap.setMana(mana); // setMana() должен быть реализован
            });
        });
        ctx.get().setPacketHandled(true);
    }
}


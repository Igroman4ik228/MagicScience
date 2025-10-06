package com.magicscience.magicsciencemod.network.mana;

import com.magicscience.magicsciencemod.network.IClientPacket;
import com.magicscience.magicsciencemod.registry.ModCapabilities;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class ClientSyncManaPacket implements IClientPacket {
    private final int mana;

    public ClientSyncManaPacket(int mana) {
        this.mana = mana;
    }

    public ClientSyncManaPacket(FriendlyByteBuf buf) {
        this.mana = buf.readVarInt();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeVarInt(mana);
    }

    @Override
    public void handle(@NotNull LocalPlayer player) {
        player.getCapability(ModCapabilities.MANA_CAPABILITY).ifPresent(cap -> {
            cap.setMana(mana);
        });
    }
}


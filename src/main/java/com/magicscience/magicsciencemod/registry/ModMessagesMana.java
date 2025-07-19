package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.net.mana.ClientboundSyncManaPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesMana {
    public static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MagicScienceMod.MOD_ID, "mana"),
            () -> "1.0",
            s -> true,
            s -> true
        );

        CHANNEL.messageBuilder(ClientboundSyncManaPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundSyncManaPacket::encode)
            .decoder(ClientboundSyncManaPacket::new)
            .consumerMainThread(ClientboundSyncManaPacket::handle)
            .add();

    }
}

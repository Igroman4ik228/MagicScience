package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.net.lightBlock.ServerboundPlaceLightBlockPacket;
import com.magicscience.magicsciencemod.net.lightBlock.ServerboundRemoveLightBlockPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesLightBlock {
    public static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MagicScienceMod.MOD_ID,"lightblock"),
            () -> "1.0",
            s -> true,
            s -> true
        );

        CHANNEL.registerMessage(packetId++,
            ServerboundPlaceLightBlockPacket.class,
            ServerboundPlaceLightBlockPacket::encode,
            ServerboundPlaceLightBlockPacket::new,
            ServerboundPlaceLightBlockPacket::handle
        );

        CHANNEL.registerMessage(packetId++,
            ServerboundRemoveLightBlockPacket.class,
            ServerboundRemoveLightBlockPacket::encode,
            ServerboundRemoveLightBlockPacket::new,
            ServerboundRemoveLightBlockPacket::handle
        );
    }
}

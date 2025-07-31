package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundCastParticlePacket;
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

        CHANNEL.registerMessage(packetId++,
            ClientboundSyncManaPacket.class,
            ClientboundSyncManaPacket::encode,
            ClientboundSyncManaPacket::new,
            ClientboundSyncManaPacket::handle
        );
    }
}

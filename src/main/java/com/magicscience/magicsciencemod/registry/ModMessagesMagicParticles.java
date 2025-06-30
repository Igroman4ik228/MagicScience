package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.net.magicparticles.ServerboundCastParticlePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesMagicParticles {
    public static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public static void register() {
        //noinspection removal
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("magicscience", "magicparticles"),
                () -> "1.0",
                s -> true,
                s -> true
        );

        CHANNEL.registerMessage(packetId++, ServerboundCastParticlePacket.class,
                ServerboundCastParticlePacket::encode,
                ServerboundCastParticlePacket::new,
                ServerboundCastParticlePacket::handle);
    }
}

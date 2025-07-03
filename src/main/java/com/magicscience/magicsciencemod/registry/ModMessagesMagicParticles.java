package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.net.magicparticles.ClientboundSpawnParticlePacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundCastParticlePacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesMagicParticles {
    public static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("magicscience", "magicparticles"),
                () -> "1.0",
                s -> true,
                s -> true
        );

        CHANNEL.registerMessage(packetId++,
                ServerboundCastParticlePacket.class,
                ServerboundCastParticlePacket::encode,
                ServerboundCastParticlePacket::new,
                ServerboundCastParticlePacket::handle
        );

        CHANNEL.registerMessage(
                packetId++,
                ClientboundSpawnParticlePacket.class,
                ClientboundSpawnParticlePacket::encode,
                ClientboundSpawnParticlePacket::new,
                ClientboundSpawnParticlePacket::handle
        );

        CHANNEL.registerMessage(
                packetId++,
                ServerboundParticleDamagePacket.class,
                ServerboundParticleDamagePacket::encode,
                ServerboundParticleDamagePacket::new,
                ServerboundParticleDamagePacket::handle
        );
    }
}

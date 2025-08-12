package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.net.magicparticles.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesMagicParticles {
    public static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MagicScienceMod.MOD_ID, "magicparticles"),
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

        CHANNEL.registerMessage(packetId++,
            ClientboundSpawnParticlePacket.class,
            ClientboundSpawnParticlePacket::encode,
            ClientboundSpawnParticlePacket::new,
            ClientboundSpawnParticlePacket::handle
        );

        CHANNEL.registerMessage(packetId++,
            ServerboundParticleDamagePacket.class,
            ServerboundParticleDamagePacket::encode,
            ServerboundParticleDamagePacket::new,
            ServerboundParticleDamagePacket::handle
        );

        CHANNEL.registerMessage(packetId++,
            ServerboundParticleEffectsPacket.class,
            ServerboundParticleEffectsPacket::encode,
            ServerboundParticleEffectsPacket::new,
            ServerboundParticleEffectsPacket::handle
        );

        CHANNEL.registerMessage(packetId++,
            ServerboundParticleBlockHitPacket.class,
            ServerboundParticleBlockHitPacket::encode,
            ServerboundParticleBlockHitPacket::new,
            ServerboundParticleBlockHitPacket::handle
        );
    }
}

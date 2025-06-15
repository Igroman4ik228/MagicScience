package com.magicscience.magicsciencemod.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesEnergy {
    public static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public static void register() {
        //noinspection removal
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("magicscience", "energy"),
                () -> "1.0",
                s -> true,
                s -> true
        );

        CHANNEL.registerMessage(packetId++, ClientboundSpawnParticlePacket.class,
                ClientboundSpawnParticlePacket::toBytes,
                ClientboundSpawnParticlePacket::new,
                ClientboundSpawnParticlePacket::handle);

        CHANNEL.registerMessage(packetId++, ServerboundCastParticlePacket.class,
                ServerboundCastParticlePacket::toBytes,
                ServerboundCastParticlePacket::new,
                ServerboundCastParticlePacket::handle);

        CHANNEL.registerMessage(packetId++, ServerboundParticleCollisionPacket.class,
                ServerboundParticleCollisionPacket::toBytes,
                ServerboundParticleCollisionPacket::new,
                ServerboundParticleCollisionPacket::handle);

    }
}


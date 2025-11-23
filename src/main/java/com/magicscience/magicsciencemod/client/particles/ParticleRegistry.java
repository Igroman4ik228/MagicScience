package com.magicscience.magicsciencemod.client.particles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ParticleRegistry {
    private static final @NotNull ConcurrentMap<UUID, MagicParticle> INSTANCES = new ConcurrentHashMap<>();

    private ParticleRegistry() {
    }

    public static void register(MagicParticle particle) {
        INSTANCES.put(particle.getParticleUUID(), particle);
    }

    public static void unregister(UUID particleUUID) {
        INSTANCES.remove(particleUUID);
    }

    public static List<MagicParticle> getActiveParticles() {
        return List.copyOf(INSTANCES.values());
    }

    @Nullable
    public static MagicParticle get(UUID particleUUID) {
        return INSTANCES.get(particleUUID);
    }
}

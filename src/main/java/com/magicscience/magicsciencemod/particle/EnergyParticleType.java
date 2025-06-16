package com.magicscience.magicsciencemod.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;

public class EnergyParticleType extends ParticleType<EnergyParticleOptions> {
    public EnergyParticleType(boolean overrideLimiter) {
        super(overrideLimiter, EnergyParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<EnergyParticleOptions> codec() {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("damage").forGetter(EnergyParticleOptions::damage),
                        Codec.INT.fieldOf("ownerId").forGetter(EnergyParticleOptions::ownerId)
                ).apply(instance, EnergyParticleOptions::new)
        );
    }
}
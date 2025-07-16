package com.magicscience.magicsciencemod.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.NotNull;

public class MagicParticleType extends ParticleType<MagicParticleOptions> {
    public MagicParticleType(boolean overrideLimiter) {
        super(overrideLimiter, MagicParticleOptions.DESERIALIZER);
    }

    @Override
    @NotNull
    public Codec<MagicParticleOptions> codec() {
        return RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.INT.fieldOf("ownerId").forGetter(MagicParticleOptions::ownerId),
                Codec.INT.fieldOf("coreId").forGetter(MagicParticleOptions::coreId),
                Codec.INT.fieldOf("coreStack").forGetter(MagicParticleOptions::coreId),
                Codec.INT.listOf().xmap(
                    list -> list.stream().mapToInt(Integer::intValue).toArray(),
                    array -> java.util.Arrays.stream(array).boxed().toList()
                ).fieldOf("attributeIds").forGetter(MagicParticleOptions::attributeIds),
                Codec.INT.listOf().xmap(
                    list -> list.stream().mapToInt(Integer::intValue).toArray(),
                    array -> java.util.Arrays.stream(array).boxed().toList()
                ).fieldOf("attributeStack").forGetter(MagicParticleOptions::attributeStack),
                Codec.INT.fieldOf("structureId").forGetter(MagicParticleOptions::structureId),
                Codec.INT.fieldOf("structureStack").forGetter(MagicParticleOptions::structureId),
                Codec.INT.fieldOf("particleSpeed").forGetter(MagicParticleOptions::particleSpeed),
                Codec.INT.fieldOf("particleLifeTime").forGetter(MagicParticleOptions::particleLifeTime)
            ).apply(instance, MagicParticleOptions::new)
        );
    }
}
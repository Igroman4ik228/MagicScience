package com.magicscience.magicsciencemod.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MagicParticleType extends ParticleType<MagicParticleOptions> {
    public MagicParticleType(boolean overrideLimiter) {
        super(overrideLimiter, MagicParticleOptions.DESERIALIZER);
    }

    @Override
    @NotNull
    public Codec<MagicParticleOptions> codec() {
        final Codec<UUID> uuidCodec = Codec.STRING.xmap(UUID::fromString, UUID::toString);

        final Codec<int[]> intArrayCodec = Codec.INT.listOf().xmap(
            list -> list.stream().mapToInt(Integer::intValue).toArray(),
            array -> java.util.Arrays.stream(array).boxed().toList()
        );

        return RecordCodecBuilder.create(instance ->
            instance.group(
                uuidCodec.fieldOf("ownerUUID").forGetter(MagicParticleOptions::ownerUUID),
                Codec.INT.fieldOf("coreId").forGetter(MagicParticleOptions::coreId),
                Codec.INT.fieldOf("coreStack").forGetter(MagicParticleOptions::coreId),
                intArrayCodec.fieldOf("attributeIds").forGetter(MagicParticleOptions::attributeIds),
                intArrayCodec.fieldOf("attributeStack").forGetter(MagicParticleOptions::attributeStack),
                Codec.INT.fieldOf("structureId").forGetter(MagicParticleOptions::structureId),
                Codec.INT.fieldOf("structureStack").forGetter(MagicParticleOptions::structureId),
                Codec.INT.fieldOf("particleSpeed").forGetter(MagicParticleOptions::particleSpeed),
                Codec.INT.fieldOf("particleLifeTime").forGetter(MagicParticleOptions::particleLifeTime)
            ).apply(instance, MagicParticleOptions::new)
        );
    }
}
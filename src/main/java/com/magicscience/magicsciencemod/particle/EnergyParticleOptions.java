package com.magicscience.magicsciencemod.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public record EnergyParticleOptions(float damage, int ownerId) implements ParticleOptions {
    public static final ParticleOptions.Deserializer<EnergyParticleOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<EnergyParticleOptions>() {
                @Override
                public EnergyParticleOptions fromCommand(ParticleType<EnergyParticleOptions> type,
                                                         StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float damage = reader.readFloat();
                    reader.expect(' ');
                    int ownerId = reader.readInt();
                    return new EnergyParticleOptions(damage, ownerId);
                }

                @Override
                public EnergyParticleOptions fromNetwork(ParticleType<EnergyParticleOptions> type,
                                                         FriendlyByteBuf buf) {
                    return new EnergyParticleOptions(buf.readFloat(), buf.readInt());
                }
            };

    @Override
    public ParticleType<?> getType() {
        return ModParticles.ENERGY_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(damage);
        buf.writeInt(ownerId);
    }

    @Override
    public String writeToString() {
        return getType() + " " + damage + " " + ownerId;
    }
}
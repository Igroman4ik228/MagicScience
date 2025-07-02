package com.magicscience.magicsciencemod.particles;

import com.magicscience.magicsciencemod.registry.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public record MagicParticleOptions(int ownerId, int coreId, int[] attributeIds, int structureId, int particleSpeed, int particleLifeTime) implements ParticleOptions {
    public static final ParticleOptions.Deserializer<MagicParticleOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {

                @Override
                public MagicParticleOptions fromCommand(ParticleType<MagicParticleOptions> type,
                                                        StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    int ownerId = reader.readInt();
                    reader.expect(' ');
                    int coreId = reader.readInt();
                    reader.expect(' ');
                    int structureId = reader.readInt();
                    reader.expect(' ');
                    int particleSpeed = reader.readInt();
                    reader.expect(' ');
                    int particleLifeTime = reader.readInt();
                    reader.expect(' ');
                    int attrCount = reader.readInt();
                    int[] attributeIds = new int[attrCount];
                    for (int i = 0; i < attrCount; i++) {
                        reader.expect(' ');
                        attributeIds[i] = reader.readInt();
                    }
                    return new MagicParticleOptions(ownerId, coreId, attributeIds, structureId, particleSpeed, particleLifeTime);
                }

                @Override
                public MagicParticleOptions fromNetwork(ParticleType<MagicParticleOptions> type,
                                                        FriendlyByteBuf buf) {
                    int ownerId = buf.readInt();
                    int coreId = buf.readInt();
                    int structureId = buf.readInt();
                    int particleSpeed = buf.readInt();
                    int particleLifeTime = buf.readInt();
                    int attrCount = buf.readVarInt();
                    int[] attributeIds = new int[attrCount];
                    for (int i = 0; i < attrCount; i++) {
                        attributeIds[i] = buf.readInt();
                    }
                    return new MagicParticleOptions(ownerId, coreId, attributeIds,
                            structureId, particleSpeed, particleLifeTime);
                }
            };


    @Override
    public ParticleType<?> getType() {
        return ModParticles.MAGIC_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(ownerId);
        buf.writeInt(coreId);
        buf.writeInt(structureId);
        buf.writeInt(particleSpeed);
        buf.writeInt(particleLifeTime);

        buf.writeVarInt(attributeIds.length);
        for (int id : attributeIds) {
            buf.writeInt(id);
        }
    }

    @Override
    public String writeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ownerId).append(" ")
                .append(coreId).append(" ")
                .append(structureId).append(" ")
                .append(particleSpeed).append(" ")
                .append(particleLifeTime).append(" ")
                .append(attributeIds.length);
        for (int id : attributeIds) {
            sb.append(" ").append(id);
        }
        return sb.toString();
    }

}

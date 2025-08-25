package com.magicscience.magicsciencemod.client.particles;

import com.magicscience.magicsciencemod.registry.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record MagicParticleOptions(
    UUID ownerUUID,
    int coreId,
    int coreStack,
    int[] attributeIds,
    int[] attributeStack,
    int structureId,
    int structureStack,
    int particleSpeed,
    int particleLifeTime
) implements ParticleOptions {
    public static final ParticleOptions.Deserializer<MagicParticleOptions> DESERIALIZER =
        new ParticleOptions.Deserializer<>() {

            @Override
            @NotNull
            public MagicParticleOptions fromCommand(
                @NotNull ParticleType<MagicParticleOptions> type,
                StringReader reader
            ) throws CommandSyntaxException {
                reader.expect(' ');
                // Читаем UUID в виде строки (формат стандартный 8-4-4-4-12)
                String uuidStr = reader.readUnquotedString();
                UUID ownerUUID = UUID.fromString(uuidStr);
                reader.expect(' ');
                int coreId = reader.readInt();
                reader.expect(' ');
                int coreStack = reader.readInt();
                reader.expect(' ');
                int structureId = reader.readInt();
                reader.expect(' ');
                int structureStack = reader.readInt();
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
                int[] attributeStack = new int[attrCount];
                for (int i = 0; i < attrCount; i++) {
                    reader.expect(' ');
                    attributeStack[i] = reader.readInt();
                }
                return new MagicParticleOptions(
                    ownerUUID,
                    coreId,
                    coreStack,
                    attributeIds,
                    attributeStack,
                    structureId,
                    structureStack,
                    particleSpeed,
                    particleLifeTime
                );
            }

            @Override
            @NotNull
            public MagicParticleOptions fromNetwork(
                @NotNull ParticleType<MagicParticleOptions> type,
                FriendlyByteBuf buf
            ) {
                UUID ownerUUID = buf.readUUID();
                int coreId = buf.readInt();
                int coreStack = buf.readInt();
                int structureId = buf.readInt();
                int structureStack = buf.readInt();
                int particleSpeed = buf.readInt();
                int particleLifeTime = buf.readInt();
                int attrCount = buf.readVarInt();
                int[] attributeIds = new int[attrCount];
                for (int i = 0; i < attrCount; i++) {
                    attributeIds[i] = buf.readInt();
                }
                int[] attributeStack = new int[attrCount];
                for (int i = 0; i < attrCount; i++) {
                    attributeStack[i] = buf.readInt();
                }
                return new MagicParticleOptions(
                    ownerUUID,
                    coreId,
                    coreStack,
                    attributeIds,
                    attributeStack,
                    structureId,
                    structureStack,
                    particleSpeed,
                    particleLifeTime
                );
            }
        };


    @Override
    @NotNull
    public ParticleType<?> getType() {
        return ModParticles.MAGIC_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeUUID(ownerUUID);
        buf.writeInt(coreId);
        buf.writeInt(coreStack);
        buf.writeInt(structureId);
        buf.writeInt(structureStack);
        buf.writeInt(particleSpeed);
        buf.writeInt(particleLifeTime);

        buf.writeVarInt(attributeIds.length);
        for (int id : attributeIds) {
            buf.writeInt(id);
        }
        for (int id : attributeStack) {
            buf.writeInt(id);
        }
    }

    @Override
    @NotNull
    public String writeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ownerUUID.toString()).append(" ")
            .append(coreId).append(" ")
            .append(coreStack).append(" ")
            .append(structureId).append(" ")
            .append(structureStack).append(" ")
            .append(particleSpeed).append(" ")
            .append(particleLifeTime).append(" ")
            .append(attributeIds.length);
        for (int id : attributeIds) {
            sb.append(" ").append(id);
        }
        for (int id : attributeStack()) {
            sb.append(" ").append(id);
        }
        return sb.toString();
    }
}

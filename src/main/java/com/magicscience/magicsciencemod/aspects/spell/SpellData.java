package com.magicscience.magicsciencemod.aspects.spell;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SpellData(
    @NotNull UUID ownerUUID,
    int coreId,
    int coreStack,
    int @NotNull [] attributeIds,
    int @NotNull [] attributeStack,
    int structureId,
    int structureStack,
    int particleSpeed,
    int particleLifeTime
) {
    public static SpellData decode(FriendlyByteBuf buf) {
        return new SpellData(
            buf.readUUID(),               // ownerUUID
            buf.readVarInt(),            // coreId
            buf.readVarInt(),            // coreStack
            buf.readVarIntArray(),       // attributeIds
            buf.readVarIntArray(),       // attributeStack
            buf.readVarInt(),            // structureId
            buf.readVarInt(),            // structureStack
            buf.readInt(),                // particleSpeed
            buf.readInt()                  // particleLifeTime
        );
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.ownerUUID());
        buf.writeVarInt(this.coreId());
        buf.writeVarInt(this.coreStack());
        buf.writeVarIntArray(this.attributeIds());
        buf.writeVarIntArray(this.attributeStack());
        buf.writeVarInt(this.structureId());
        buf.writeVarInt(this.structureStack());
        buf.writeInt(this.particleSpeed());
        buf.writeInt(this.particleLifeTime());
    }
}

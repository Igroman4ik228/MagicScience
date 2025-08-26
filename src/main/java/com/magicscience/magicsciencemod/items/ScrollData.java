package com.magicscience.magicsciencemod.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record ScrollData(
    @Nullable UUID authorUUID,
    int coreId,
    int coreStack,
    int @NotNull [] attributeIds,
    int @NotNull [] attributeStack,
    int structureId,
    int structureStack,
    int particleSpeed,
    int particleLifeTime
) {
    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        ScrollData that = (ScrollData) o;
        return coreId==that.coreId &&
            coreStack==that.coreStack &&
            structureId==that.structureId &&
            structureStack==that.structureStack &&
            particleSpeed==that.particleSpeed &&
            particleLifeTime==that.particleLifeTime &&
            Arrays.equals(attributeIds, that.attributeIds) &&
            Arrays.equals(attributeStack, that.attributeStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coreId, coreStack, structureId, structureStack, particleSpeed, particleLifeTime,
            Arrays.hashCode(attributeIds), Arrays.hashCode(attributeStack));
    }
}

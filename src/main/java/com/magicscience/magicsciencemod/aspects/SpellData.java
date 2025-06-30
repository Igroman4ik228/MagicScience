package com.magicscience.magicsciencemod.aspects;

public record SpellData(
        int ownerId,
        int coreId,
        int[] attributeIds,
        int structureId,
        int particleSpeed) {
}

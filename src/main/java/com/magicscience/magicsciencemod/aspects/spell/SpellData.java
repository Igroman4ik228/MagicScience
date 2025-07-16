package com.magicscience.magicsciencemod.aspects.spell;

import org.jetbrains.annotations.NotNull;

public record SpellData(
    int ownerId,
    int coreId,
    int coreStack,
    int @NotNull [] attributeIds,
    int @NotNull [] attributeStack,
    int structureId,
    int structureStack,
    int particleSpeed,
    int particleLifeTime) {
}

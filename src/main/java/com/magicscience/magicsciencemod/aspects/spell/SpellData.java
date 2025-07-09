package com.magicscience.magicsciencemod.aspects.spell;

import org.jetbrains.annotations.NotNull;

public record SpellData(
    int ownerId,
    int coreId,
    int @NotNull [] attributeIds,
    int structureId,
    int particleSpeed,
    int particleLifeTime) {
}

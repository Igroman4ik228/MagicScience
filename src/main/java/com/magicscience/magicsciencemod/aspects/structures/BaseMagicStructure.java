package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMagicStructure implements IMagicStructure {
    private final @NotNull BaseStructureData baseStructureData;
    private final int stack;

    public BaseMagicStructure(
        @NotNull BaseStructureData baseStructureData,
        int stack
    ) {
        this.baseStructureData = baseStructureData;
        this.stack = Math.max(1, stack);
    }

    @Override
    public int getManaCost() {
        return baseStructureData.manaCost();
    }

    @Override
    public int getCountParticles() {
        return baseStructureData.countParticles() * stack;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public int getSize() {
        return baseStructureData.size() * stack;
    }

    @NotNull
    public abstract Vec3 calculateStartParticlePosition(@NotNull Vec3 basePosition);
}

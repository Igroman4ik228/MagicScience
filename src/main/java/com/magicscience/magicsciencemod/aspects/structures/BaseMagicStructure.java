package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMagicStructure implements IMagicStructure {
    private final int manaCost;
    private final int countParticles;
    private final int size;
    private final int stack;

    public BaseMagicStructure(
        int manaCost,
        int countParticles,
        int size,
        int stack
    ) {
        this.manaCost = manaCost;
        this.countParticles = countParticles;
        this.size = size;

        if (stack < 1) {
            stack = 1;
        }
        this.stack = stack;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public int getCountParticles() {
        return countParticles * stack;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public int getSize() {
        return size * stack;
    }

    @NotNull
    public abstract Vec3 calculateStartParticlePosition(Vec3 basePosition);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

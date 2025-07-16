package com.magicscience.magicsciencemod.aspects.structures;

public abstract class BaseMagicStructure implements IMagicStructure {
    private final int manaCost;
    private final int countParticles;
    private final int spawnParticlesRadius;
    private final int stack;

    public BaseMagicStructure(
        int manaCost,
        int countParticles,
        int spawnParticlesRadius,
        int stack
    ) {
        this.manaCost = manaCost;
        this.countParticles = countParticles;
        this.spawnParticlesRadius = spawnParticlesRadius;

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
        return countParticles;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public int getSpawnParticlesRadius() {
        return spawnParticlesRadius;
    }
}

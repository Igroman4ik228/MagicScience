package com.magicscience.magicsciencemod.aspects.structures;

public abstract class BaseMagicStructure implements IMagicStructure {
    private final int manaCost;
    private final int countParticles;
    private final int spawnParticlesRadius;

    public BaseMagicStructure(
        int manaCost,
        int countParticles,
        int spawnParticlesRadius
    ) {
        this.manaCost = manaCost;
        this.countParticles = countParticles;
        this.spawnParticlesRadius = spawnParticlesRadius;
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
    public int getSpawnParticlesRadius() {
        return spawnParticlesRadius;
    }
}

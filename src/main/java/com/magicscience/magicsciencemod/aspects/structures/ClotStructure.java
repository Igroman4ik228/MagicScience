package com.magicscience.magicsciencemod.aspects.structures;

public class ClotStructure implements IMagicStructure {
    public int manaCost;

    public int countParticles;
    public int spawnParticlesRadius;

    public ClotStructure(int manaCost, int countParticles, int spawnParticlesRadius) {
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

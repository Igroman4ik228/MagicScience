package com.magicscience.magicsciencemod.aspects.structures;

public class ClotStructure implements IMagicStructure {
    public int manaCost;

    public int countParticles;
    public int spawnParticlesRadius;

    private final StructureTypes structureType;

    public ClotStructure(int manaCost, int countParticles, int spawnParticlesRadius) {
        this.manaCost = manaCost;

        this.countParticles = countParticles;
        this.spawnParticlesRadius = spawnParticlesRadius;

        this.structureType = StructureTypes.CLOT;
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

    @Override
    public int getCode() {
        return structureType.getCode();
    }
}

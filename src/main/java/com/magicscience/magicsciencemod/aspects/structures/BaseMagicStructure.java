package com.magicscience.magicsciencemod.aspects.structures;

public abstract class BaseMagicStructure implements IMagicStructure {
    private final int manaCost;
    private final int countParticles;
    private final int spawnParticlesRadius;
    private final StructureTypes structureType;

    public BaseMagicStructure(
        int manaCost,
        int countParticles,
        int spawnParticlesRadius,
        StructureTypes structureType
    ) {
        this.manaCost = manaCost;
        this.countParticles = countParticles;
        this.spawnParticlesRadius = spawnParticlesRadius;
        this.structureType = structureType;
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
    public int getId() {
        return structureType.getId();
    }

    @Override
    public StructureTypes getType() {
        return structureType;
    }
}

package com.magicscience.magicsciencemod.aspects.structures;

public class ClotStructure extends BaseMagicStructure {

    public ClotStructure() {
        this(30, 20, 10);
    }

    public ClotStructure(int manaCost, int countParticles, int spawnParticlesRadius) {
        super(manaCost, countParticles, spawnParticlesRadius);
    }
}

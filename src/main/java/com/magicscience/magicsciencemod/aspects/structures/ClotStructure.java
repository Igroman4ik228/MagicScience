package com.magicscience.magicsciencemod.aspects.structures;

public class ClotStructure extends BaseMagicStructure {

    public ClotStructure() {
        this(30, 20, 10, 1);
    }

    public ClotStructure(int stack) {
        this(30, 20, 10, stack);
    }

    public ClotStructure(int manaCost, int countParticles, int spawnParticlesRadius, int stack) {
        super(manaCost, countParticles, spawnParticlesRadius, stack);
    }

    @Override
    public IMagicStructure cloneWithArguments(Object... args) {
        return new ClotStructure((int) args[0]);
    }
}

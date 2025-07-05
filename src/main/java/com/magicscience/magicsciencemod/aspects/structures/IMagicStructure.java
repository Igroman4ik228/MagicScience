package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;

public interface IMagicStructure extends IMagicAspect {
    int getCountParticles();

    int getSpawnParticlesRadius();

    int getId();

    StructureTypes getType();
}

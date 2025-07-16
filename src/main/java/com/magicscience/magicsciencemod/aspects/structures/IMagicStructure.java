package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;

public interface IMagicStructure extends IMagicAspect {
    int getCountParticles();
    IMagicStructure cloneWithArguments(Object... args);
    int getStack();
    int getSpawnParticlesRadius();
}

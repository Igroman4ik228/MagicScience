package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.cores.effects.BaseMagicEffect;

import java.util.Collection;

public interface IMagicCore extends IMagicAspect {
    int getDamage();

    int getParticleLifeTime();

    int getParticleCount();

    float getSize();

    Collection<BaseMagicEffect> getMagicEffects();
}

package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IMagicCore extends IMagicAspect {
    int getDamage();

    int getParticleLifeTime();

    int getParticleCount();

    float getSize();

    int getStack();

    IMagicCore cloneWithArguments(Object... args);

    @NotNull Collection<IMagicEffect> getMagicEffects();
}

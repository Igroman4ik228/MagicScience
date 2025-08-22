package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class BaseMagicCore implements IMagicCore {
    private final @NotNull BaseCoreData baseCoreData;
    private final int stack;
    private final @NotNull Collection<IMagicEffect> effects;

    public BaseMagicCore(
        @NotNull BaseCoreData baseCoreData,
        int stack,
        @NotNull Collection<IMagicEffect> effects
    ) {
        this.baseCoreData = baseCoreData;
        this.stack = Math.max(1, stack);
        this.effects = effects;
    }

    @Override
    public int getManaCost() {
        return baseCoreData.manaCost() * stack;
    }

    @Override
    public int getDamage() {
        return baseCoreData.damage() * stack;
    }

    @Override
    public int getParticleLifeTime() {
        return baseCoreData.particleLifeTime();
    }

    @Override
    public int getParticleCount() {
        return baseCoreData.particleCount();
    }

    @Override
    public float getSize() {
        return baseCoreData.size() * stack;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    @NotNull
    public Collection<IMagicEffect> getMagicEffects() {
        return effects;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null) return false;
        return this.getClass()==o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class BaseMagicCore implements IMagicCore {
    private final int manaCost;
    private final int damage;
    private final int particleLifeTime;
    private final int particleCount;
    private final float size;
    private final int stack;
    private final @NotNull Collection<IMagicEffect> effects;

    public BaseMagicCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        float size,
        int stack,
        @NotNull Collection<IMagicEffect> effects
    ) {
        this.manaCost = manaCost;
        this.damage = damage;
        this.particleLifeTime = particleLifeTime;
        this.particleCount = particleCount;
        this.size = size;
        this.stack = Math.max(1, stack);
        this.effects = effects;
    }

    @Override
    public int getManaCost() {
        return manaCost * stack;
    }

    @Override
    public int getDamage() {
        return damage * stack;
    }

    @Override
    public int getParticleLifeTime() {
        return particleLifeTime;
    }

    @Override
    public int getParticleCount() {
        return particleCount;
    }

    @Override
    public float getSize() {
        return size * stack;
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

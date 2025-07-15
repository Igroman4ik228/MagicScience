package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class BaseMagicCore implements IMagicCore {
    private final int manaCost;
    private final int damage;
    private final int particleLifeTime;
    private final int particleCount;
    private final float size;
    
    private @NotNull Collection<IMagicEffect> effects = new ArrayList<>();

    public BaseMagicCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        float size
    ) {
        this.manaCost = manaCost;
        this.damage = damage;
        this.particleLifeTime = particleLifeTime;
        this.particleCount = particleCount;
        this.size = size;
    }

    protected void setEffects(@NotNull Collection<IMagicEffect> effects) {
        this.effects = effects;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public int getDamage() {
        return damage;
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
        return size;
    }

    @Override
    @NotNull
    public Collection<IMagicEffect> getMagicEffects() {
        return effects;
    }
}

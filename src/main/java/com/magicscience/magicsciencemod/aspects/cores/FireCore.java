package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.BornEffect;

import java.util.List;

public class FireCore extends BaseMagicCore {
    public FireCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        float size,
        int stack
    ) {
        super(manaCost, damage, particleLifeTime, particleCount, size, stack);
        setEffects(List.of(new BornEffect()));
    }

    public FireCore() {
        this(10, 10, 300, 10, 1, 1);
    }

    public FireCore(int stack) {
        this(10, 10, 300, 10, 1, stack);
    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new FireCore((int) args[0]);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FireCore;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

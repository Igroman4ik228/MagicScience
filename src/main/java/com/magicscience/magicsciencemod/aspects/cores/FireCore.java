package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.BornEffect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class FireCore extends BaseMagicCore {
    public FireCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        float size,
        int stack,
        @NotNull Collection<IMagicEffect> effects
    ) {
        super(manaCost, damage, particleLifeTime, particleCount, size, stack, effects);
    }

    public FireCore() {
        this(10, 10, 300, 10, 1, 1, List.of(new BornEffect()));
    }

    public FireCore(int stack) {
        this(10, 10, 300, 10, 1, stack, List.of(new BornEffect()));
    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new FireCore((int) args[0]);
    }
}

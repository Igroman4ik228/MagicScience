package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.BornEffect;

import java.util.List;

public class FireCore extends BaseMagicCore {
    public FireCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        float size
    ) {
        super(manaCost, damage, particleLifeTime, particleCount, size);
        setEffects(List.of(new BornEffect()));
    }

    public FireCore() {
        this(10, 10, 300, 10, 1);
    }
}

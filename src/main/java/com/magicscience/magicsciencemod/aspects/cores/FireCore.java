package com.magicscience.magicsciencemod.aspects.cores;

public class FireCore extends BaseMagicCore {
    public FireCore() {
        this(10, 10, 300, 10, 1);
    }

    public FireCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        float size
    ) {
        super(manaCost, damage, particleLifeTime, particleCount, size);
    }
}

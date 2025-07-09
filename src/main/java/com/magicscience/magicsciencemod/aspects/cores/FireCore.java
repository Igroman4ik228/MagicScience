package com.magicscience.magicsciencemod.aspects.cores;

public class FireCore extends BaseMagicCore {
    public FireCore() {
        this(10, 10, 300);
    }

    public FireCore(int manaCost, int damage, int particleLifeTime) {
        super(manaCost, damage, particleLifeTime);
    }
}

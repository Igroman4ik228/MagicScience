package com.magicscience.magicsciencemod.aspects.cores;

public class BaseMagicCore implements IMagicCore {
    private final int manaCost;
    private final int damage;
    private final int particleLifeTime;

    public BaseMagicCore(
        int manaCost,
        int damage,
        int particleLifeTime
    ) {
        this.manaCost = manaCost;
        this.damage = damage;
        this.particleLifeTime = particleLifeTime;
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
}

package com.magicscience.magicsciencemod.aspects.cores;

public class FireCore implements IMagicCore {
    public int manaCost;

    public int damage;
    public int particleLifeTime;

    private final CoreTypes coreType;

    public FireCore() {
        this(10, 10, 30);
    }

    public FireCore(int manaCost, int damage, int particleLifeTime) {
        this.manaCost = manaCost;

        this.damage = damage;
        this.particleLifeTime = particleLifeTime;

        this.coreType = CoreTypes.FIRE;
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
    public int getCode() {
        return coreType.getCode();
    }
}

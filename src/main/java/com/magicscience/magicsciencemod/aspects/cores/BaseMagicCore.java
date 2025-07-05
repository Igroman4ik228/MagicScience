package com.magicscience.magicsciencemod.aspects.cores;

public class BaseMagicCore implements IMagicCore {
    private final int manaCost;
    private final int damage;
    private final int particleLifeTime;
    private final CoreTypes coreType;

    public BaseMagicCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        CoreTypes coreType
    ) {
        this.manaCost = manaCost;
        this.damage = damage;
        this.particleLifeTime = particleLifeTime;
        this.coreType = coreType;
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
    public int getTypeId() {
        return coreType.getId();
    }

    @Override
    public CoreTypes getType() {
        return coreType;
    }
}

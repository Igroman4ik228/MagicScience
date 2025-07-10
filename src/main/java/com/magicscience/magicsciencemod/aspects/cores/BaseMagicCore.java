package com.magicscience.magicsciencemod.aspects.cores;

public class BaseMagicCore implements IMagicCore {
    private final int manaCost;
    private final int damage;
    private final int particleLifeTime;
    private final int particleCount;
    private final float size;

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
}

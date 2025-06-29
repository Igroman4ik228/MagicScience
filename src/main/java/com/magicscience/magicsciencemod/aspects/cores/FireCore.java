package com.magicscience.magicsciencemod.aspects.cores;

public class FireCore implements IMagicCore {
    public int manaCost;

    public int damage;
    public int particleLifeTime;


    public FireCore(int manaCost, int damage, int particleLifeTime) {
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

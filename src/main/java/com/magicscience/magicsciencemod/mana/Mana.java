package com.magicscience.magicsciencemod.mana;

public class Mana implements IMana {
    private int mana = 0;
    private int maxMana = 100;

    @Override
    public int getMana() { return mana; }

    @Override
    public int getMaxMana() { return maxMana; }

    @Override
    public void setMana(int mana) {
        this.mana = Math.min(mana, maxMana);
    }

    @Override
    public void addMana(int amount) {
        setMana(this.mana + amount);
    }

    @Override
    public void removeMana(int amount) {
        setMana(this.mana - amount);
    }

    @Override
    public void setMaxMana(int max) {
        this.maxMana = max;
    }
}


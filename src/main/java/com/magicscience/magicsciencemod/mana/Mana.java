package com.magicscience.magicsciencemod.mana;

public class Mana implements IMana {
    // Default for create world
    private int mana = 0;
    private int maxMana = 100;

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int amount) {
        this.mana = Math.max(0, Math.min(amount, maxMana));
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMaxMana(int max) {
        this.maxMana = max;
    }

    @Override
    public void addMana(int amount) {
        setMana(this.mana + amount);
    }

    @Override
    public void removeMana(int amount) {
        setMana(this.mana - amount);
    }
}


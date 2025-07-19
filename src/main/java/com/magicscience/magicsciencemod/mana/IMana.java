package com.magicscience.magicsciencemod.mana;

public interface IMana {
    int getMana();
    int getMaxMana();
    void setMana(int mana);
    void addMana(int amount);
    void removeMana(int amount);
    void setMaxMana(int max);
}

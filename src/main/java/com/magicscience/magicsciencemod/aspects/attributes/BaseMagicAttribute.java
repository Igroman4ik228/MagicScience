package com.magicscience.magicsciencemod.aspects.attributes;

public abstract class BaseMagicAttribute implements IMagicAttribute {
    private final int manaCost;

    protected BaseMagicAttribute(int manaCost) {
        this.manaCost = manaCost;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }
}

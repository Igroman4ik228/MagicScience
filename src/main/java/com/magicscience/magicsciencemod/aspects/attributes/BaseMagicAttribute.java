package com.magicscience.magicsciencemod.aspects.attributes;

public abstract class BaseMagicAttribute implements IMagicAttribute {
    private final int manaCost;
    private final int stack;

    protected BaseMagicAttribute(int manaCost, int stack) {
        this.manaCost = manaCost;

        if (stack < 1) {
            stack = 1;
        }
        this.stack = stack;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public int getManaCost() {
        return manaCost * stack;
    }
}

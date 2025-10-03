package com.magicscience.magicsciencemod.aspects.attributes;

import org.jetbrains.annotations.NotNull;

public abstract class BaseMagicAttribute implements IMagicAttribute {
    private final @NotNull BaseAttributeData baseAttributeData;
    private final int stack;

    protected BaseMagicAttribute(@NotNull BaseAttributeData baseAttributeData, int stack) {
        this.baseAttributeData = baseAttributeData;
        this.stack = Math.max(1, stack);
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public int getManaCost() {
        return baseAttributeData.manaCost() * stack;
    }
}

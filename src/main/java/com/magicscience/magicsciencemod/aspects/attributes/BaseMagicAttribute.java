package com.magicscience.magicsciencemod.aspects.attributes;

public abstract class BaseMagicAttribute implements IMagicAttribute {
    private final BaseAttributeData baseAttributeData;
    private final int stack;

    protected BaseMagicAttribute(BaseAttributeData baseAttributeData, int stack) {
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

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null) return false;
        return this.getClass()==o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

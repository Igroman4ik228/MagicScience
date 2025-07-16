package com.magicscience.magicsciencemod.aspects.attributes;

public class VectorAttribute extends BaseMagicAttribute {
    public VectorAttribute() {
        this(20, 1);
    }

    public VectorAttribute(int stack) {
        super(20, 1);
    }

    public VectorAttribute(int manaCost, int stack) {
        super(manaCost, stack);
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new VectorAttribute((int) args[0]);
    }
}

package com.magicscience.magicsciencemod.aspects.attributes;

public class VectorAttribute extends BaseMagicAttribute {
    public VectorAttribute() {
        this(20);
    }

    public VectorAttribute(int manaCost) {
        super(manaCost, AttributeTypes.VECTOR);
    }
}

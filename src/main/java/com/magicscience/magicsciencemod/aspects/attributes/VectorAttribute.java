package com.magicscience.magicsciencemod.aspects.attributes;

public class VectorAttribute implements IMagicAttribute {
    private final int manaCost;
    private final AttributeTypes attributeType;

    public VectorAttribute() {
        manaCost = 20;
        attributeType = AttributeTypes.VECTOR;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public int getTypeCode() {
        return attributeType.getCode();
    }

    @Override
    public AttributeTypes getType() {
        return attributeType;
    }
}

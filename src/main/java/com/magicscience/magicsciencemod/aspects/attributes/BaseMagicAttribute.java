package com.magicscience.magicsciencemod.aspects.attributes;

public abstract class BaseMagicAttribute implements IMagicAttribute {
    private final int manaCost;
    private final AttributeTypes attributeType;

    protected BaseMagicAttribute(int manaCost, AttributeTypes attributeType) {
        this.manaCost = manaCost;
        this.attributeType = attributeType;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public int getTypeId() {
        return attributeType.getId();
    }

    @Override
    public AttributeTypes getType() {
        return attributeType;
    }
}

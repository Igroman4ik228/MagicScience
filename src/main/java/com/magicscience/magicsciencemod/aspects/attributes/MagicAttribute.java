package com.magicscience.magicsciencemod.aspects.attributes;

public class MagicAttribute implements IMagicAttribute {

    private final int manaCost;

    private final AttributeTypes attributeTypes;

    public MagicAttribute(int attributeCode) {
        switch (attributeCode) {
            case 0 -> {
                this.manaCost = 10;
                this.attributeTypes = AttributeTypes.VECTOR;
            }
            default -> throw new IllegalArgumentException("Unknown attribute code: " + attributeCode);
        }
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    public AttributeTypes getAttributeTypes() {
        return attributeTypes;
    }
}

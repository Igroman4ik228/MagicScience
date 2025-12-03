package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeType;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;

public class MagicAttributeFactory extends BaseMagicFactory<IMagicAttribute, AttributeType> {
    public MagicAttributeFactory() {
        super(AttributeType.class);
    }
}

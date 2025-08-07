package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;

public class MagicAttributeFactory extends BaseMagicFactory<IMagicAttribute, AttributeTypes> {
    public MagicAttributeFactory() {
        super(AttributeTypes.class);
    }
}

package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;

public interface IMagicAttribute extends IMagicAspect {
    public int getManaCost();
    public int getTypeCode();
    public AttributeTypes getType();
}

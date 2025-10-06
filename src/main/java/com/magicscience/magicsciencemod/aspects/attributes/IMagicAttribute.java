package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;

public interface IMagicAttribute extends IMagicAspect {
    int getManaCost();

    int getStack();

    IMagicAttribute cloneWithArguments(Object... args);
}

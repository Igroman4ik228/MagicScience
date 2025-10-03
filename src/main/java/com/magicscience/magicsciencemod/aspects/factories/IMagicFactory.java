package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.IMagicType;

public interface IMagicFactory<T extends IMagicAspect, E extends Enum<E> & IMagicType<T>> {
    T create(E type, Object... args);

    T createById(int id, Object... args);
}

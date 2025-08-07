package com.magicscience.magicsciencemod.aspects.factories;

public interface IMagicFactory<T, E extends Enum<E> & IMagicType<T>> {
    T create(E type, Object... args);

    T createById(int id, Object... args);
}

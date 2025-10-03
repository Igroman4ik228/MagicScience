package com.magicscience.magicsciencemod.aspects;

public interface IMagicType<T extends IMagicAspect> {
    T getInstance();

    T newInstance(Object... args);

    Class<? extends T> getTypeClass();

    int getId();
}

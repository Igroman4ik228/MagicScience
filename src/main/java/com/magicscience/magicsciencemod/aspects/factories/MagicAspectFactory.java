package com.magicscience.magicsciencemod.aspects.factories;

public class MagicAspectFactory<T, E extends Enum<E> & IMagicType<T>> implements IMagicFactory<T, E> {

    @Override
    public T create(E type, Object... args) {
        return type.newInstance(args);
    }

    @Override
    public T createById(int id, Class<E> enumClass, Object... args) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getId() == id) {
                return create(e, args);
            }
        }
        throw new IllegalArgumentException("Unknown id: " + id);
    }
}


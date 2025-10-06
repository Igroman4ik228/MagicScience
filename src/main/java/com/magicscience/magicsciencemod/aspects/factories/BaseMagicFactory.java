package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.IMagicType;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMagicFactory<T extends IMagicAspect, E extends Enum<E> & IMagicType<T>> implements IMagicFactory<T, E> {
    private final @NotNull Class<E> enumClass;

    protected BaseMagicFactory(@NotNull Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T create(@NotNull E type, Object... args) {
        return type.newInstance(args);
    }

    @Override
    public T createById(int id, Object... args) {
        for (E e : this.enumClass.getEnumConstants()) {
            if (e.getId()==id) {
                return create(e, args);
            }
        }
        throw new IllegalArgumentException("Unknown id: " + id);
    }
}

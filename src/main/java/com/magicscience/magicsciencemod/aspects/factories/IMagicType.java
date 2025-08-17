package com.magicscience.magicsciencemod.aspects.factories;

import java.util.Objects;

public interface IMagicType<T> {
    static <E extends Enum<E> & IMagicType<T>, T> T findInstance(int id, Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId()==id)
                return type.getInstance();
        }
        throw new IllegalArgumentException("Failed to find instance by ID: " + id);
    }

    static <E extends Enum<E> & IMagicType<T>, T> int findId(T instance, Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (Objects.equals(type.getInstance(), instance))
                return type.getId();
        }
        throw new IllegalArgumentException("Failed to find ID for instance: " +
            (instance!=null ? instance.getClass().getName():null));
    }

    static <E extends Enum<E> & IMagicType<?>> E findType(int id, Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId()==id)
                return type;
        }
        throw new IllegalArgumentException("Invalid ID: " + id + " for enum " + enumClass.getSimpleName());
    }

    int getId();

    T getInstance();

    T newInstance(Object... args);
}

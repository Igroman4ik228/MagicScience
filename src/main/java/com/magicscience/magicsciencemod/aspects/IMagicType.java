package com.magicscience.magicsciencemod.aspects;

import java.util.Objects;

public interface IMagicType<T> {
    /**
     * Unified search by ID
     */
    static <E extends Enum<E> & IMagicType<T>, T> T findInstance(int id, Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId() == id)
                return type.getInstance();
        }
        throw new IllegalArgumentException("Failed to find instance by ID: " + id);
    }

    /**
     * Unified search for ID by instance
     */
    static <E extends Enum<E> & IMagicType<T>, T> int findId(T value, Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (Objects.equals(type.getInstance(), value))
                return type.getId();
        }
        throw new IllegalArgumentException("Failed to find ID for instance: " +
            (value != null ? value.getClass().getName() : null));
    }

    /**
     * Returns the numeric identifier of the constant
     */
    int getId();

    /**
     * Returns the associated instance
     */
    T getInstance();
}
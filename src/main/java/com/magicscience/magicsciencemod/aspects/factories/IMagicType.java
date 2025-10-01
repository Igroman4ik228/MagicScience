package com.magicscience.magicsciencemod.aspects.factories;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface IMagicType<T> {
    @NotNull
    static <E extends Enum<E> & IMagicType<T>, T> T findInstance(int id, @NotNull Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId() == id)
                return type.getInstance();
        }

        throw new IllegalArgumentException("Failed to find instance by ID: " + id);
    }

    static <E extends Enum<E> & IMagicType<T>, T> int findId(@Nullable T instance, @NotNull Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (Objects.equals(type.getInstance(), instance))
                return type.getId();
        }

        throw new IllegalArgumentException("Failed to find ID for instance: " +
            (instance != null ? instance.getClass().getName() : null));
    }

    @NotNull
    static <E extends Enum<E> & IMagicType<?>> E findType(int id, @NotNull Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId() == id)
                return type;
        }

        throw new IllegalArgumentException("Invalid ID: " + id + " for enum " + enumClass.getSimpleName());
    }

    int getId();

    T getInstance();

    T newInstance(Object... args);
}

package com.magicscience.magicsciencemod.aspects;

import org.jetbrains.annotations.NotNull;

public class MagicTypeHelper {
    @NotNull
    public static <E extends Enum<E> & IMagicType<T>, T extends IMagicAspect> T findInstance(int id, @NotNull Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId()==id)
                return type.getInstance();
        }

        throw new IllegalArgumentException("Failed to find instance by ID: " + id);
    }

    public static <E extends Enum<E> & IMagicType<T>, T extends IMagicAspect> int findId(@NotNull T instance, @NotNull Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getTypeClass()==instance.getClass())
                return type.getId();
        }

        throw new IllegalArgumentException("Failed to find ID for instance: " + instance.getClass().getName());
    }

    @NotNull
    public static <E extends Enum<E> & IMagicType<T>, T extends IMagicAspect> E findType(int id, @NotNull Class<E> enumClass) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getId()==id)
                return type;
        }

        throw new IllegalArgumentException("Invalid ID: " + id + " for enum " + enumClass.getSimpleName());
    }
}

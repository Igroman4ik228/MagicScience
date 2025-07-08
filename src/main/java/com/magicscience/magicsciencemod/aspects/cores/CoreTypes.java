package com.magicscience.magicsciencemod.aspects.cores;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum CoreTypes {
    FIRE(1, new FireCore());

    private final int id;
    private final @NotNull IMagicCore instance;

    CoreTypes(int id, @NotNull IMagicCore instance) {
        this.id = id;
        this.instance = instance;
    }

    @NotNull
    public static IMagicCore getInstance(int id) {
        for (CoreTypes type : values()) {
            if (type.id == id)
                return type.instance;
        }
        throw new IllegalArgumentException("Не удалось найти экземпляр по ID: " + id);
    }

    public static int getId(@NotNull IMagicCore instance) {
        for (CoreTypes type : values()) {
            if (Objects.equals(type.instance, instance))
                return type.id;
        }
        throw new IllegalArgumentException("Не удалось найти ID для экземпляра: " + instance.getClass().getName());
    }

    @NotNull
    public IMagicCore getInstance() {
        return instance;
    }

    public int getId() {
        return id;
    }
}

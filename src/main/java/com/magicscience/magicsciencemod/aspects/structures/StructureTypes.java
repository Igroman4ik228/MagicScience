package com.magicscience.magicsciencemod.aspects.structures;

import javax.annotation.Nullable;
import java.util.Objects;

public enum StructureTypes {
    NONE(0),
    CLOT(1, new ClotStructure());

    private final int id;
    private final @Nullable IMagicStructure instance;

    StructureTypes(int id) {
        this(id, null);
    }

    StructureTypes(int id, @Nullable IMagicStructure instance) {
        this.id = id;
        this.instance = instance;
    }

    @Nullable
    public static IMagicStructure getInstance(int id) {
        for (StructureTypes type : values()) {
            if (type.id == id)
                return type.instance;
        }
        throw new IllegalArgumentException("Не удалось найти экземпляр по ID: " + id);
    }

    public static int getId(@Nullable IMagicStructure instance) {
        for (StructureTypes type : values()) {
            if (Objects.equals(type.instance, instance))
                return type.id;
        }
        throw new IllegalArgumentException("Не удалось найти ID для экземпляра: " + (instance != null ? instance.getClass().getName() : null));
    }

    @Nullable
    public IMagicStructure getInstance() {
        return instance;
    }

    public int getId() {
        return id;
    }
}

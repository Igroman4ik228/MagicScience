package com.magicscience.magicsciencemod.aspects.attributes;

import javax.annotation.Nullable;
import java.util.Objects;

public enum AttributeTypes {
    NONE(0),
    VECTOR(1, new VectorAttribute()),
    SELF_SPECTRE(2, new SelfSpectreAttribute());

    private final int id;
    private final @Nullable IMagicAttribute instance;

    AttributeTypes(int id) {
        this(id, null);
    }

    AttributeTypes(int id, @Nullable IMagicAttribute instance) {
        this.id = id;
        this.instance = instance;
    }

    @Nullable
    public static IMagicAttribute getInstance(int id) {
        for (AttributeTypes type : values()) {
            if (type.id == id)
                return type.instance;
        }
        throw new IllegalArgumentException("Не удалось найти экземпляр по ID: " + id);
    }

    public static int getId(@Nullable IMagicAttribute instance) {
        for (AttributeTypes type : values()) {
            if (Objects.equals(type.instance, instance))
                return type.id;
        }
        throw new IllegalArgumentException("Не удалось найти ID для экземпляра: " + (instance != null ? instance.getClass().getName() : null));
    }

    @Nullable
    public IMagicAttribute getInstance() {
        return instance;
    }

    public int getId() {
        return id;
    }
}

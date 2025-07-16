package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.IMagicType;

import javax.annotation.Nullable;

public enum AttributeTypes implements IMagicType<IMagicAttribute> {
    NONE(),
    VECTOR(new VectorAttribute()),
    SELF_SPECTRE(new SelfSpectreAttribute());

    private final @Nullable IMagicAttribute prototype;

    AttributeTypes() {
        this.prototype = null;
    }

    AttributeTypes(@Nullable IMagicAttribute prototype) {
        this.prototype = prototype;
    }

    @Nullable
    public static IMagicAttribute getInstance(int id) {
        return IMagicType.findInstance(id, AttributeTypes.class);
    }

    @Nullable
    public static IMagicAttribute getInstance(int id, Object... args) {
        for (AttributeTypes type : values()) {
            if (type.getId() == id)
                return type.newInstance(args);
        }
        throw new IllegalArgumentException("Unknown attribute id: " + id);
    }

    public static int getId(@Nullable IMagicAttribute instance) {
        return IMagicType.findId(instance, AttributeTypes.class);
    }

    @Override
    @Nullable
    public IMagicAttribute getInstance() {
        return prototype;
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    @Nullable
    public IMagicAttribute newInstance(Object... args) {
        return prototype != null ? prototype.cloneWithArguments(args) : null;
    }
}

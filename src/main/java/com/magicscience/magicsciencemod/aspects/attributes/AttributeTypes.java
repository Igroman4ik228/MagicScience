package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.IMagicType;

import javax.annotation.Nullable;

public enum AttributeTypes implements IMagicType<IMagicAttribute> {
    NONE(),
    VECTOR(new VectorAttribute()),
    SELF_SPECTRE(new SelfSpectreAttribute());

    private final @Nullable IMagicAttribute instance;

    AttributeTypes() {
        this(null);
    }

    AttributeTypes(@Nullable IMagicAttribute instance) {
        this.instance = instance;
    }

    @Nullable
    public static IMagicAttribute getInstance(int id) {
        return IMagicType.findInstance(id, AttributeTypes.class);
    }

    public static int getId(@Nullable IMagicAttribute instance) {
        return IMagicType.findId(instance, AttributeTypes.class);
    }

    @Override
    @Nullable
    public IMagicAttribute getInstance() {
        return instance;
    }

    @Override
    public int getId() {
        return ordinal();
    }
}

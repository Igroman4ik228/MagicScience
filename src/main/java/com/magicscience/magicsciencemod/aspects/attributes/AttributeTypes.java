package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.IMagicType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum AttributeTypes implements IMagicType<IMagicAttribute> {
    NONE(null),
    VECTOR(VectorAttribute::new),
    SELF_SPECTRE(SelfSpectreAttribute::new),
    SPREADING(SpreadingAttribute::new),
    GRAVITY(GravityAttribute::new),
    DIVISION(DivisionAttribute::new);

    private final @Nullable Supplier<IMagicAttribute> prototype;

    AttributeTypes(@Nullable Supplier<IMagicAttribute> prototype) {
        this.prototype = prototype;
    }

    @Override
    @Nullable
    public IMagicAttribute getInstance() {
        if (prototype==null) return null;
        return prototype.get();
    }

    @Override
    @Nullable
    public IMagicAttribute newInstance(Object... args) {
        if (prototype==null) return null;
        return prototype.get().cloneWithArguments(args);
    }

    @Override
    @Nullable
    public Class<? extends IMagicAttribute> getTypeClass() {
        if (prototype==null) return null;
        return prototype.get().getClass();
    }

    @Override
    public int getId() {
        return ordinal();
    }
}


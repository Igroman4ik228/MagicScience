package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;

public enum AttributeTypes implements IMagicType<IMagicAttribute> {
    NONE(null),
    VECTOR(new VectorAttribute()),
    SELF_SPECTRE(new SelfSpectreAttribute());

    private final IMagicAttribute prototype;

    AttributeTypes(IMagicAttribute prototype) {
        this.prototype = prototype;
    }

    public IMagicAttribute getPrototype() {
        return prototype;
    }

    @Override
    public IMagicAttribute getInstance() {
        return prototype;
    }

    @Override
    public IMagicAttribute newInstance(Object... args) {
        return prototype.cloneWithArguments(args);
    }

    @Override
    public int getId() {
        return ordinal();
    }
}


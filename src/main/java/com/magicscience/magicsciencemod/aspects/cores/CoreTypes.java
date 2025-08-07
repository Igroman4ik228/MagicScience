package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;

public enum CoreTypes implements IMagicType<IMagicCore> {
    FIRE(new FireCore());

    private final IMagicCore prototype;

    CoreTypes(IMagicCore prototype) {
        this.prototype = prototype;
    }

    public IMagicCore getPrototype() {
        return prototype;
    }

    @Override
    public IMagicCore getInstance() {
        return prototype;
    }

    @Override
    public IMagicCore newInstance(Object... args) {
        return prototype.cloneWithArguments(args);
    }

    @Override
    public int getId() {
        return ordinal() + 1;
    }
}

package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IMagicParticleSpeed;

public class VectorAttribute extends BaseMagicAttribute implements IMagicParticleSpeed {
    public VectorAttribute() {
        this(20, 1);
    }

    public VectorAttribute(int stack) {
        super(20, stack);
    }

    public VectorAttribute(int manaCost, int stack) {
        super(manaCost, stack);
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new VectorAttribute((int) args[0]);
    }

    @Override
    public int getParticleSpeed() {
        return 2 * getStack();
    }
}

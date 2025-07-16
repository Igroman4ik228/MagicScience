package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IMagicParticleSpeed;

public class VectorAttribute extends BaseMagicAttribute implements IMagicParticleSpeed {
    private final int particleSpeed;

    public VectorAttribute(int manaCost, int stack, int particleSpeed) {
        super(manaCost, stack);
        this.particleSpeed = particleSpeed;
    }

    public VectorAttribute() {
        this(20, 1,2);
    }

    public VectorAttribute(int stack) {
        this(20, 1, 2);
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new VectorAttribute((int) args[0]);
    }

    @Override
    public int getParticleSpeed() {
        return particleSpeed * getStack();
    }
}

package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IMagicParticleSpeed;

public class VectorAttribute extends BaseMagicAttribute implements IMagicParticleSpeed {
    private final int particleSpeed;

    public VectorAttribute(int manaCost, int particleSpeed) {
        super(manaCost);
        this.particleSpeed = particleSpeed;
    }

    public VectorAttribute() {
        this(20, 2);
    }

    @Override
    public int getParticleSpeed() {
        return particleSpeed;
    }
}

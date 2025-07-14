package com.magicscience.magicsciencemod.aspects.attributes;

// unique class for add particle speed
public class VectorAttribute extends BaseMagicAttribute {
    public static final int PARTICLE_SPEED = 2;
    
    public VectorAttribute() {
        this(20);
    }

    public VectorAttribute(int manaCost) {
        super(manaCost);
    }
}

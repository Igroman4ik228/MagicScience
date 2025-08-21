package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;

public enum StructureTypes implements IMagicType<IMagicStructure> {
    NONE(null),
    CLOT(new ClotStructure()),
    SPHERE(new SphereStructure()),
    WALL(new WallStructure()),
    RAY(new RayStructure());

    private final IMagicStructure prototype;

    StructureTypes(IMagicStructure prototype) {
        this.prototype = prototype;
    }

    public IMagicStructure getPrototype() {
        return prototype;
    }

    @Override
    public IMagicStructure getInstance() {
        return prototype;
    }

    @Override
    public IMagicStructure newInstance(Object... args) {
        return prototype.cloneWithArguments(args);
    }

    @Override
    public int getId() {
        return ordinal();
    }
}

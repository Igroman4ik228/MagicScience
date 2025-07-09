package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicType;

import javax.annotation.Nullable;

public enum StructureTypes implements IMagicType<IMagicStructure> {
    NONE(),
    CLOT(new ClotStructure());

    private final @Nullable IMagicStructure instance;

    StructureTypes() {
        this(null);
    }

    StructureTypes(@Nullable IMagicStructure instance) {
        this.instance = instance;
    }

    @Nullable
    public static IMagicStructure getInstance(int id) {
        return IMagicType.findInstance(id, StructureTypes.class);
    }

    public static int getId(@Nullable IMagicStructure instance) {
        return IMagicType.findId(instance, StructureTypes.class);
    }

    @Nullable
    public IMagicStructure getInstance() {
        return instance;
    }

    public int getId() {
        return ordinal();
    }
}

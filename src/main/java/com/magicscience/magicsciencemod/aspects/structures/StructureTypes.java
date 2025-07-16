package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicType;

import javax.annotation.Nullable;

public enum StructureTypes implements IMagicType<IMagicStructure> {
    NONE(),
    CLOT(new ClotStructure());

    private final @Nullable IMagicStructure prototype;

    StructureTypes() {
        this.prototype = null;
    }

    StructureTypes(@Nullable IMagicStructure prototype) {
        this.prototype = prototype;
    }

    @Nullable
    public static IMagicStructure getInstance(int id) {
        return IMagicType.findInstance(id, StructureTypes.class);
    }

    @Nullable
    public static IMagicStructure getInstance(int id, Object... args) {
        for (StructureTypes type : values()) {
            if (type.getId() == id)
                return type.newInstance(args);
        }
        throw new IllegalArgumentException("Unknown structure id: " + id);
    }

    public static int getId(@Nullable IMagicStructure instance) {
        return IMagicType.findId(instance, StructureTypes.class);
    }

    @Override
    @Nullable
    public IMagicStructure getInstance() {
        return prototype;
    }

    @Override
    @Nullable
    public IMagicStructure newInstance(Object... args) {
        return prototype != null ? prototype.cloneWithArguments(args) : null;
    }

    @Override
    public int getId() {
        return ordinal();
    }
}

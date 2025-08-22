package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum StructureTypes implements IMagicType<IMagicStructure> {
    NONE(null),
    CLOT(ClotStructure::new),
    SPHERE(SphereStructure::new);

    private final @Nullable Supplier<IMagicStructure> prototype;

    StructureTypes(@Nullable Supplier<IMagicStructure> prototype) {
        this.prototype = prototype;
    }

    @Override
    @Nullable
    public IMagicStructure getInstance() {
        if (prototype==null) return null;
        return prototype.get();
    }

    @Override
    @Nullable
    public IMagicStructure newInstance(Object... args) {
        if (prototype==null) return null;
        return prototype.get().cloneWithArguments(args);
    }

    @Override
    public int getId() {
        return ordinal();
    }
}

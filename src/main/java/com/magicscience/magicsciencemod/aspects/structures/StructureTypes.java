package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicType;
import com.magicscience.magicsciencemod.aspects.structures.dynamic.ConeStructure;
import com.magicscience.magicsciencemod.aspects.structures.dynamic.WaveStructure;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum StructureTypes implements IMagicType<IMagicStructure> {
    NONE(null),
    CLOT(ClotStructure::new),
    SPHERE(SphereStructure::new),
    WALL(WallStructure::new),
    RAY(RayStructure::new),
    WAVE(WaveStructure::new),
    CONE(ConeStructure::new);

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
    @Nullable
    public Class<? extends IMagicStructure> getTypeClass() {
        if (prototype==null) return null;
        return prototype.get().getClass();
    }

    @Override
    public int getId() {
        return ordinal();
    }
}

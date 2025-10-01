package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;
import org.jetbrains.annotations.NotNull;

public final class StructureTypeHelper {
    private static final Class<StructureTypes> enumClass = StructureTypes.class;

    @NotNull
    public static IMagicStructure findInstance(int id) {
        return IMagicType.findInstance(id, enumClass);
    }

    public static int findId(@NotNull IMagicStructure structure) {
        return IMagicType.findId(structure, enumClass);
    }

    @NotNull
    public static StructureTypes findType(int id) {
        return IMagicType.findType(id, enumClass);
    }
}
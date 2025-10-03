package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.MagicTypeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StructureTypeHelper {
    private static final Class<StructureTypes> enumClass = StructureTypes.class;

    @Nullable
    public static IMagicStructure findInstance(int id) {
        if (id==StructureTypes.NONE.getId())
            return null;

        return MagicTypeHelper.findInstance(id, enumClass);
    }

    public static int findId(@Nullable IMagicStructure structure) {
        if (structure==null)
            return StructureTypes.NONE.getId();

        return MagicTypeHelper.findId(structure, enumClass);
    }

    @NotNull
    public static StructureTypes findType(int id) {
        return MagicTypeHelper.findType(id, enumClass);
    }
}
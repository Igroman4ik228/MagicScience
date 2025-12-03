package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.MagicTypeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StructureTypeHelper {
    private static final Class<StructureType> enumClass = StructureType.class;

    @Nullable
    public static IMagicStructure findInstance(int id) {
        if (id==StructureType.NONE.getId())
            return null;

        return MagicTypeHelper.findInstance(id, enumClass);
    }

    public static int findId(@Nullable IMagicStructure structure) {
        if (structure==null)
            return StructureType.NONE.getId();

        return MagicTypeHelper.findId(structure, enumClass);
    }

    @NotNull
    public static StructureType findType(int id) {
        return MagicTypeHelper.findType(id, enumClass);
    }
}
package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.MagicTypeHelper;
import org.jetbrains.annotations.NotNull;

public final class CoreTypeHelper {
    private static final Class<CoreTypes> enumClass = CoreTypes.class;

    @NotNull
    public static IMagicCore findInstance(int id) {
        return MagicTypeHelper.findInstance(id, enumClass);
    }

    public static int findId(@NotNull IMagicCore core) {
        return MagicTypeHelper.findId(core, enumClass);
    }

    @NotNull
    public static CoreTypes findType(int id) {
        return MagicTypeHelper.findType(id, enumClass);
    }
}


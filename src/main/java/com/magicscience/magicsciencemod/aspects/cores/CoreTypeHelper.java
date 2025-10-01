package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CoreTypeHelper {
    private static final Class<CoreTypes> enumClass = CoreTypes.class;

    @NotNull
    public static IMagicCore findInstance(int id) {
        return IMagicType.findInstance(id, enumClass);
    }

    public static int findId(@Nullable IMagicCore core) {
        return IMagicType.findId(core, enumClass);
    }

    @NotNull
    public static CoreTypes findType(int id) {
        return IMagicType.findType(id, enumClass);
    }
}


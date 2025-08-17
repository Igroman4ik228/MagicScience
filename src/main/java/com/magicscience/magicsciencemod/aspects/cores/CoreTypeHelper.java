package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;

public final class CoreTypeHelper {
    private static final Class<CoreTypes> enumClass = CoreTypes.class;

    public static IMagicCore findInstance(int id) {
        return IMagicType.findInstance(id, enumClass);
    }

    public static int findId(IMagicCore core) {
        return IMagicType.findId(core, enumClass);
    }

    public static CoreTypes findType(int id) {
        return IMagicType.findType(id, enumClass);
    }
}


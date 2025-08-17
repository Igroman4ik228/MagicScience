package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;

public final class StructureTypeHelper {
    private static final Class<StructureTypes> enumClass = StructureTypes.class;

    public static IMagicStructure findInstance(int id) {
        return IMagicType.findInstance(id, enumClass);
    }

    public static int findId(IMagicStructure structure) {
        return IMagicType.findId(structure, enumClass);
    }

    public static StructureTypes findType(int id) {
        return IMagicType.findType(id, enumClass);
    }

    public static StructureTypes getType(int id) {
        return IMagicType.findType(id, enumClass);
    }


}
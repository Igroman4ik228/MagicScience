package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;

public final class AttributeTypeHelper {
    private static final Class<AttributeTypes> enumClass = AttributeTypes.class;

    public static IMagicAttribute findInstance(int id) {
        return IMagicType.findInstance(id, enumClass);
    }

    public static int findId(IMagicAttribute attribute) {
        return IMagicType.findId(attribute, enumClass);
    }

    public static AttributeTypes findType(int id) {
        return IMagicType.findType(id, enumClass);
    }
}

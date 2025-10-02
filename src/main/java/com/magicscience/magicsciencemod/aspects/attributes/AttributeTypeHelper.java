package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AttributeTypeHelper {
    private static final Class<AttributeTypes> enumClass = AttributeTypes.class;

    @Nullable
    public static IMagicAttribute findInstance(int id) {
        try {
            return IMagicType.findInstance(id, enumClass);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static int findId(@Nullable IMagicAttribute attribute) {
        return IMagicType.findId(attribute, enumClass);
    }

    @NotNull
    public static AttributeTypes findType(int id) {
        return IMagicType.findType(id, enumClass);
    }
}

package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.MagicTypeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AttributeTypeHelper {
    private static final Class<AttributeTypes> enumClass = AttributeTypes.class;

    @Nullable
    public static IMagicAttribute findInstance(int id) {
        if (id==AttributeTypes.NONE.getId())
            return null;

        return MagicTypeHelper.findInstance(id, enumClass);
    }

    public static int findId(@Nullable IMagicAttribute attribute) {
        if (attribute==null)
            return AttributeTypes.NONE.getId();

        return MagicTypeHelper.findId(attribute, enumClass);
    }

    @NotNull
    public static AttributeTypes findType(int id) {
        return MagicTypeHelper.findType(id, enumClass);
    }
}

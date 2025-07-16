package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicType;
import org.jetbrains.annotations.NotNull;

public enum CoreTypes implements IMagicType<IMagicCore> {
    FIRE(new FireCore());

    private final @NotNull IMagicCore prototype;

    CoreTypes(@NotNull IMagicCore prototype) {
        this.prototype = prototype;
    }

    @Override
    public @NotNull IMagicCore getInstance() {
        return prototype;
    }

    @Override
    public @NotNull IMagicCore newInstance(Object... args) {
        return prototype.cloneWithArguments(args);
    }

    @Override
    public int getId() {
        return ordinal() + 1;
    }

    public static int getId(@NotNull IMagicCore instance) {
        return IMagicType.findId(instance, CoreTypes.class);
    }

    public static IMagicCore getInstance(int id) {
        return IMagicType.findInstance(id, CoreTypes.class);
    }

    public static IMagicCore getInstance(int id, Object... args) {
        for (CoreTypes type : values()) {
            if (type.getId() == id)
                return type.newInstance(args);
        }
        throw new IllegalArgumentException("Unknown core id: " + id);
    }
}

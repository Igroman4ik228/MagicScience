package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicType;
import org.jetbrains.annotations.NotNull;

public enum CoreTypes implements IMagicType<IMagicCore> {
    FIRE(new FireCore());

    private final @NotNull IMagicCore instance;

    CoreTypes(@NotNull IMagicCore instance) {
        this.instance = instance;
    }

    @NotNull
    public static IMagicCore getInstance(int id) {
        return IMagicType.findInstance(id, CoreTypes.class);
    }

    public static int getId(@NotNull IMagicCore instance) {
        return IMagicType.findId(instance, CoreTypes.class);
    }

    @NotNull
    public IMagicCore getInstance() {
        return instance;
    }

    public int getId() {
        // Not index, because not null
        return ordinal() + 1;
    }
}

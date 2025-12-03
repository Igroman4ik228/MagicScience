package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum CoreType implements IMagicType<IMagicCore> {
    FIRE(FireCore::new),
    GROUND(GroundCore::new),
    WATER(WaterCore::new);

    private final @NotNull Supplier<IMagicCore> prototype;

    CoreType(@NotNull Supplier<IMagicCore> prototype) {
        this.prototype = prototype;
    }

    @Override
    @NotNull
    public IMagicCore getInstance() {
        return prototype.get();
    }

    @Override
    @NotNull
    public IMagicCore newInstance(Object... args) {
        return prototype.get().cloneWithArguments(args);
    }

    @Override
    @NotNull
    public Class<? extends IMagicCore> getTypeClass() {
        return prototype.get().getClass();
    }

    @Override
    public int getId() {
        // +1 because don't have NONE (core is required)
        return ordinal() + 1;
    }
}

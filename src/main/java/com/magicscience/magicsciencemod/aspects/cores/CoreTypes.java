package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.factories.IMagicType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum CoreTypes implements IMagicType<IMagicCore> {
    FIRE(FireCore::new);

    private final @NotNull Supplier<IMagicCore> prototype;

    CoreTypes(@NotNull Supplier<IMagicCore> prototype) {
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
    public int getId() {
        return ordinal() + 1;
    }
}

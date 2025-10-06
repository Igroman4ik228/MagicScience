package com.magicscience.magicsciencemod.config.server;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.IMagicType;
import org.jetbrains.annotations.NotNull;

public interface IAspectConfig<A extends IMagicAspect, T extends IMagicType<A>, D> {
    @NotNull
    T getType();

    @NotNull
    D toData();
}
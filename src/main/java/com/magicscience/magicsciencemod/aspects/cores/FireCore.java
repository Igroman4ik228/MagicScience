package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.BornEffect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.config.server.core.CoreConfig;
import com.magicscience.magicsciencemod.config.server.core.FireCoreConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class FireCore extends BaseMagicCore {
    private static final FireCoreConfig CONFIG = (FireCoreConfig) CoreConfig.get(CoreTypes.FIRE);

    private final int burnDuration;

    public FireCore(
        @NotNull BaseCoreData baseCoreData,
        int stack,
        int burnDuration,
        @NotNull Collection<IMagicEffect> effects
    ) {
        super(baseCoreData, stack, effects);
        this.burnDuration = burnDuration;
    }

    public FireCore() {
        this(CONFIG.toData(), 1, CONFIG.getBurnDuration(), List.of(new BornEffect(CONFIG.getBurnDuration())));
    }

    public FireCore(int stack) {
        this(CONFIG.toData(), stack, CONFIG.getBurnDuration(), List.of(new BornEffect(CONFIG.getBurnDuration())));
    }

    public int getBurnDuration() {
        return burnDuration;
    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new FireCore((int) args[0]);
    }
}

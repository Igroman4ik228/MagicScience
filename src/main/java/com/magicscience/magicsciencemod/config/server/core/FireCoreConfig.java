package com.magicscience.magicsciencemod.config.server.core;

import com.magicscience.magicsciencemod.aspects.cores.BaseCoreData;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class FireCoreConfig extends BaseCoreConfig {
    private final ForgeConfigSpec.IntValue burnDuration;

    public FireCoreConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull CoreTypes sectionType,
        @NotNull BaseCoreData defaults
    ) {
        super(builder, sectionType, defaults, false);

        burnDuration = builder
            .comment("fire burn duration in ticks")
            .defineInRange("burnDuration", 100, 0, Integer.MAX_VALUE);

        builder.pop();
    }

    public int getBurnDuration() {
        return burnDuration.get();
    }
}

package com.magicscience.magicsciencemod.config.server.core;

import com.magicscience.magicsciencemod.aspects.cores.BaseCoreData;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class GroundCoreConfig extends BaseCoreConfig {

    public GroundCoreConfig(
            @NotNull ForgeConfigSpec.Builder builder,
            @NotNull CoreTypes sectionType,
            @NotNull BaseCoreData defaults
    ) {
        super(builder, sectionType, defaults, false);


        builder.pop();
    }
}

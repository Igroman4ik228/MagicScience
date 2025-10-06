package com.magicscience.magicsciencemod.config.server.attribute;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.BaseAttributeData;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class DivisionAttributeConfig extends BaseAttributeConfig {
    private final @NotNull ForgeConfigSpec.IntValue multiplier;

    public DivisionAttributeConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull AttributeTypes sectionType,
        @NotNull BaseAttributeData defaults
    ) {
        super(builder, sectionType, defaults, false);

        multiplier = builder
            .comment("particle multiplier")
            .defineInRange("multiplier", 2, 1, Integer.MAX_VALUE);

        builder.pop();
    }

    public int getMultiplier() {
        return multiplier.get();
    }
}

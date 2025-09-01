package com.magicscience.magicsciencemod.config.server.attribute;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.BaseAttributeData;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class SubsidenceAttributeConfig extends BaseAttributeConfig {
    private final @NotNull ForgeConfigSpec.DoubleValue gravity;

    public SubsidenceAttributeConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull AttributeTypes sectionType,
        @NotNull BaseAttributeData defaults
    ) {
        super(builder, sectionType, defaults, false);

        gravity = builder
            .comment("particle gravity")
            .defineInRange("gravity", 0.1, 0, Double.MAX_VALUE);

        builder.pop();
    }

    public float getGravity() {
        return gravity.get().floatValue();
    }
}

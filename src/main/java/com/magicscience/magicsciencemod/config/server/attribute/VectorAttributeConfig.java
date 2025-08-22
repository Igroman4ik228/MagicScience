package com.magicscience.magicsciencemod.config.server.attribute;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.BaseAttributeData;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class VectorAttributeConfig extends BaseAttributeConfig {
    private final @NotNull ForgeConfigSpec.IntValue particleSpeed;

    public VectorAttributeConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull AttributeTypes sectionType,
        @NotNull BaseAttributeData defaults
    ) {
        super(builder, sectionType, defaults, false);

        particleSpeed = builder
            .comment("particle speed")
            .defineInRange("particleSpeed", 1, 1, Integer.MAX_VALUE);

        builder.pop();
    }

    public int getParticleSpeed() {
        return particleSpeed.get();
    }
}

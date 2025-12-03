package com.magicscience.magicsciencemod.config.server.attribute;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeType;
import com.magicscience.magicsciencemod.aspects.attributes.BaseAttributeData;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class BaseAttributeConfig implements IBaseAttributeConfig {
    private final @NotNull AttributeType sectionType;

    private final @NotNull ForgeConfigSpec.IntValue manaCost;

    public BaseAttributeConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull AttributeType sectionType,
        @NotNull BaseAttributeData defaults,
        boolean autoPop
    ) {
        this.sectionType = sectionType;

        builder.push(sectionType.toString().toLowerCase());

        manaCost = builder
            .comment("mana cost per 1 stack")
            .defineInRange("manaCost", defaults.manaCost(), 0, Integer.MAX_VALUE);

        if (autoPop)
            builder.pop();
    }

    public BaseAttributeConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull AttributeType sectionType,
        @NotNull BaseAttributeData defaults
    ) {
        this(builder, sectionType, defaults, true);
    }

    @Override
    @NotNull
    public AttributeType getType() {
        return sectionType;
    }

    @NotNull
    public BaseAttributeData toData() {
        return new BaseAttributeData(
            manaCost.get()
        );
    }
}

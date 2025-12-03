package com.magicscience.magicsciencemod.config.server.attribute;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeType;
import com.magicscience.magicsciencemod.aspects.attributes.BaseAttributeData;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class AttributeConfig {
    private static final @NotNull Map<AttributeType, IBaseAttributeConfig> CONFIGS = new EnumMap<>(AttributeType.class);

    private static void add(@NotNull IBaseAttributeConfig config) {
        CONFIGS.put(config.getType(), config);
    }

    @NotNull
    public static IBaseAttributeConfig get(@NotNull AttributeType sectionType) {
        var cfg = CONFIGS.get(sectionType);
        if (cfg==null)
            throw new IllegalStateException("No attribute config for type: " + sectionType);
        return cfg;
    }

    public static void register(@NotNull ForgeConfigSpec.Builder builder) {
        builder.push("attributes");

        add(
            new VectorAttributeConfig(
                builder,
                AttributeType.VECTOR,
                new BaseAttributeData(10)
            )
        );
        add(
            new BaseAttributeConfig(
                builder,
                AttributeType.SELF_SPECTRE,
                new BaseAttributeData(10)
            )
        );
        add(
            new BaseAttributeConfig(
                builder,
                AttributeType.SPREADING,
                new BaseAttributeData(10)
            )
        );
        add(
            new GravityAttributeConfig(
                builder,
                AttributeType.GRAVITY,
                new BaseAttributeData(10)
            )
        );
        add(
            new DivisionAttributeConfig(
                builder,
                AttributeType.DIVISION,
                new BaseAttributeData(10)
            )
        );

        builder.pop();
    }
}

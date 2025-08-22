package com.magicscience.magicsciencemod.config.server.structure;

import com.magicscience.magicsciencemod.aspects.structures.BaseStructureData;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class StructureConfig {
    private static final @NotNull Map<StructureTypes, IBaseStructureConfig> CONFIGS = new EnumMap<>(StructureTypes.class);

    private static void add(@NotNull IBaseStructureConfig config) {
        CONFIGS.put(config.getType(), config);
    }

    @NotNull
    public static IBaseStructureConfig get(@NotNull StructureTypes sectionType) {
        var cfg = CONFIGS.get(sectionType);
        if (cfg==null)
            throw new IllegalStateException("No structure config for type: " + sectionType);
        return cfg;
    }

    public static void register(@NotNull ForgeConfigSpec.Builder builder) {
        builder.push("structure");

        add(
            new BaseStructureConfig(
                builder,
                StructureTypes.CLOT,
                new BaseStructureData(20, 10, 1)
            )
        );

        add(
            new BaseStructureConfig(
                builder,
                StructureTypes.SPHERE,
                new BaseStructureData(30, 30, 1)
            )
        );

        builder.pop();
    }
}

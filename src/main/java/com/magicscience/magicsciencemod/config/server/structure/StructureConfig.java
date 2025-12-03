package com.magicscience.magicsciencemod.config.server.structure;

import com.magicscience.magicsciencemod.aspects.structures.BaseStructureData;
import com.magicscience.magicsciencemod.aspects.structures.StructureType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class StructureConfig {
    private static final @NotNull Map<StructureType, IBaseStructureConfig> CONFIGS = new EnumMap<>(StructureType.class);

    private static void add(@NotNull IBaseStructureConfig config) {
        CONFIGS.put(config.getType(), config);
    }

    @NotNull
    public static IBaseStructureConfig get(@NotNull StructureType sectionType) {
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
                StructureType.CLOT,
                new BaseStructureData(20, 10, 1)
            )
        );
        add(
            new BaseStructureConfig(
                builder,
                StructureType.SPHERE,
                new BaseStructureData(30, 30, 1)
            )
        );
        add(
            new BaseStructureConfig(
                builder,
                StructureType.RAY,
                new BaseStructureData(20, 15, 1)
            )
        );
        add(
            new BaseStructureConfig(
                builder,
                StructureType.WALL,
                new BaseStructureData(20, 10, 1)
            )
        );
        add(
            new BaseStructureConfig(
                builder,
                StructureType.WAVE,
                new BaseStructureData(30, 25, 1)
            )
        );
        add(
            new BaseStructureConfig(
                builder,
                StructureType.CONE,
                new BaseStructureData(30, 25, 1)
            )
        );

        builder.pop();
    }
}

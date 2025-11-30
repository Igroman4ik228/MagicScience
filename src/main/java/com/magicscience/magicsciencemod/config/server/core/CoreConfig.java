package com.magicscience.magicsciencemod.config.server.core;

import com.magicscience.magicsciencemod.aspects.cores.BaseCoreData;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class CoreConfig {
    private static final @NotNull Map<CoreTypes, IBaseCoreConfig> CONFIGS = new EnumMap<>(CoreTypes.class);

    private static void add(@NotNull IBaseCoreConfig config) {
        CONFIGS.put(config.getType(), config);
    }

    @NotNull
    public static IBaseCoreConfig get(@NotNull CoreTypes sectionType) {
        var cfg = CONFIGS.get(sectionType);
        if (cfg==null)
            throw new IllegalStateException("No core config for type: " + sectionType);
        return cfg;
    }

    public static void register(@NotNull ForgeConfigSpec.Builder builder) {
        builder.push("cores");

        add(
            new FireCoreConfig(
                builder,
                CoreTypes.FIRE,
                new BaseCoreData(10, 12, 300, 12, 1.0f)
            )
        );
        add(
                new GroundCoreConfig(
                        builder,
                        CoreTypes.GROUND,
                        new BaseCoreData(15, 8, 300, 10, 1.0f)
                )
        );

        builder.pop();
    }
}

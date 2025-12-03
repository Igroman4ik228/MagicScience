package com.magicscience.magicsciencemod.config.server.core;

import com.magicscience.magicsciencemod.aspects.cores.BaseCoreData;
import com.magicscience.magicsciencemod.aspects.cores.CoreType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class BaseCoreConfig implements IBaseCoreConfig {
    private final @NotNull CoreType sectionType;

    private final @NotNull ForgeConfigSpec.IntValue manaCost;
    private final @NotNull ForgeConfigSpec.IntValue damage;
    private final @NotNull ForgeConfigSpec.IntValue particleLifeTime;
    private final @NotNull ForgeConfigSpec.IntValue particleCount;
    private final @NotNull ForgeConfigSpec.DoubleValue size;

    public BaseCoreConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull CoreType sectionType,
        @NotNull BaseCoreData defaults,
        boolean autoPop
    ) {
        this.sectionType = sectionType;

        builder.push(sectionType.toString().toLowerCase());

        manaCost = builder
            .comment("mana cost per 1 stack")
            .defineInRange("manaCost", defaults.manaCost(), 0, Integer.MAX_VALUE);
        damage = builder
            .comment("damage per 1 stack")
            .defineInRange("damage", defaults.damage(), 0, Integer.MAX_VALUE);
        particleLifeTime = builder
            .comment("particle lifetime (ticks)")
            .defineInRange("particleLifeTime", defaults.particleLifeTime(), 0, Integer.MAX_VALUE);
        particleCount = builder
            .comment("particle count")
            .defineInRange("particleCount", defaults.particleCount(), 0, Integer.MAX_VALUE);
        size = builder
            .comment("particle size multiplier per 1 stack")
            .defineInRange("size", defaults.size(), 0d, Double.POSITIVE_INFINITY);

        if (autoPop)
            builder.pop();
    }

    public BaseCoreConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull CoreType sectionType,
        @NotNull BaseCoreData defaults
    ) {
        this(builder, sectionType, defaults, true);
    }

    @Override
    @NotNull
    public CoreType getType() {
        return sectionType;
    }

    @NotNull
    public BaseCoreData toData() {
        return new BaseCoreData(
            manaCost.get(),
            damage.get(),
            particleLifeTime.get(),
            particleCount.get(),
            size.get().floatValue()
        );
    }
}

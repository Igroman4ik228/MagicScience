package com.magicscience.magicsciencemod.config.server.structure;

import com.magicscience.magicsciencemod.aspects.structures.BaseStructureData;
import com.magicscience.magicsciencemod.aspects.structures.StructureType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

public class BaseStructureConfig implements IBaseStructureConfig {
    private final @NotNull StructureType sectionType;

    private final @NotNull ForgeConfigSpec.IntValue manaCost;
    private final @NotNull ForgeConfigSpec.IntValue countParticles;
    private final @NotNull ForgeConfigSpec.IntValue size;

    public BaseStructureConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull StructureType sectionType,
        @NotNull BaseStructureData defaults,
        boolean autoPop
    ) {
        this.sectionType = sectionType;

        builder.push(sectionType.toString().toLowerCase());

        manaCost = builder
            .comment("mana cost per 1 stack")
            .defineInRange("manaCost", defaults.manaCost(), 0, Integer.MAX_VALUE);
        countParticles = builder
            .comment("count particles")
            .defineInRange("countParticles", defaults.countParticles(), 0, Integer.MAX_VALUE);
        size = builder
            .comment("size")
            .defineInRange("size", defaults.size(), 0, Integer.MAX_VALUE);

        if (autoPop)
            builder.pop();
    }

    public BaseStructureConfig(
        @NotNull ForgeConfigSpec.Builder builder,
        @NotNull StructureType sectionType,
        @NotNull BaseStructureData defaults
    ) {
        this(builder, sectionType, defaults, true);
    }

    @Override
    @NotNull
    public StructureType getType() {
        return sectionType;
    }

    @NotNull
    public BaseStructureData toData() {
        return new BaseStructureData(
            manaCost.get(),
            countParticles.get(),
            size.get()
        );
    }
}

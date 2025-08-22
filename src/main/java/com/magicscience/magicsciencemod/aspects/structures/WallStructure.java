package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WallStructure extends BaseMagicStructure {
    public WallStructure(BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public WallStructure() {
        this(new BaseStructureData(20, 10, 1), 1);
    }

    public WallStructure(int stack) {
        this(new BaseStructureData(20, 10, 1), stack);
    }

    @Override
    @NotNull
    public IMagicStructure cloneWithArguments(Object... args) {
        return new WallStructure((int) args[0]);
    }

    @Override
    @NotNull
    public Vec3 calculateStartParticlePosition(Vec3 basePosition) {
        return null;
    }
}

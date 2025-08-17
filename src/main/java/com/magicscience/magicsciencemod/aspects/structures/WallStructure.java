package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WallStructure extends BaseMagicStructure {
    public WallStructure(int manaCost, int countParticles, int size, int stack) {
        super(manaCost, countParticles, size, stack);
    }

    public WallStructure() {
        this(20, 10, 1, 1);
    }

    public WallStructure(int stack) {
        this(20, 10, 1, stack);
    }

    @Override
    public @NotNull IMagicStructure cloneWithArguments(Object... args) {
        return new WallStructure((int) args[0]);
    }

    @Override
    public @NotNull Vec3 calculateStartParticlePosition(Vec3 basePosition) {
        return null;
    }
}

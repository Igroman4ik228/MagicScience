package com.magicscience.magicsciencemod.aspects.structures.dynamic;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface IDynamicMagicStructure {
    @NotNull
    Vec3 calculateStartParticleVectors(@NotNull DynamicStructureContext context);
}

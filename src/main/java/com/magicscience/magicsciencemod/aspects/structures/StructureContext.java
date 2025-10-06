package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record StructureContext(
    @NotNull RandomSource random,
    @NotNull Vec3 basePosition,
    @NotNull Vec3 eyePosition,
    @NotNull Vec3 lookAngel
) {
}

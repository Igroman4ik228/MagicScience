package com.magicscience.magicsciencemod.aspects.structures.dynamic;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public record DynamicStructureContext(
    Random random,
    @NotNull Vec3 startPosition,
    @NotNull Vec3 eyePosition,
    @NotNull Vec3 lookDirection,
    @NotNull Vec3 centerPosition
) {
}

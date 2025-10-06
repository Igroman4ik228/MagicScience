package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public record StructureContext(
    Random random,
    @NotNull Vec3 basePosition,
    @NotNull Vec3 eyePosition,
    @NotNull Vec3 lookAngel
) {
}

package com.magicscience.magicsciencemod.aspects.cores.collisions.IActions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FunctionalInterface
public interface IActionBlock {
    void execute(
        @NotNull BlockHitResult blockHitResult,
        @NotNull ServerPlayer sender,
        Objects... objects
    );
}

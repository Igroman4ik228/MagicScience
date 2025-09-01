package com.magicscience.magicsciencemod.aspects.cores.collisions.IActions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FunctionalInterface
public interface IActionEntity {
    void execute(
        @NotNull Entity entity,
        @NotNull ServerPlayer sender,
        Objects... objects
    );
}

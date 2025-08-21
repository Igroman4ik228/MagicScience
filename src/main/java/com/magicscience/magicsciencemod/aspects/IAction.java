package com.magicscience.magicsciencemod.aspects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;

@FunctionalInterface
public interface IAction {
    void execute(Level level, BlockHitResult blockHitResult, ServerPlayer sender, Objects... objects);
}

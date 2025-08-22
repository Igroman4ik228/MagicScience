package com.magicscience.magicsciencemod.aspects;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;

@FunctionalInterface
public interface IAction {
    void execute(BlockHitResult blockHitResult, Player sender, Objects... objects);
}

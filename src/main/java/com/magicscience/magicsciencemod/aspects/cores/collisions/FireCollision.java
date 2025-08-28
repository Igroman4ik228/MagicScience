package com.magicscience.magicsciencemod.aspects.cores.collisions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FireCollision {
    public static void collisionTnt(
        @NotNull BlockHitResult blockHitResult,
        @NotNull ServerPlayer sender,
        Objects... objects
    ) {
        var level = sender.level();
        var blockPos = blockHitResult.getBlockPos();
        level.removeBlock(blockPos, false);

        var centerBlockPos = blockPos.getCenter();
        PrimedTnt primed = new PrimedTnt(
            level,
            centerBlockPos.x,
            centerBlockPos.y,
            centerBlockPos.z,
            sender
        );
        level.addFreshEntity(primed);
    }
}

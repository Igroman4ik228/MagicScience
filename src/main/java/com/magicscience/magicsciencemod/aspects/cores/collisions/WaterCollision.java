package com.magicscience.magicsciencemod.aspects.cores.collisions;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

public class WaterCollision {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void collisionFire(
        @NotNull BlockHitResult blockHitResult,
        @NotNull ServerPlayer sender,
        Objects... objects
    ) {
        var level = sender.level();
        var blockPos = blockHitResult.getBlockPos();

        // Replace fire to air (put out the fire)
        level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
    }
}

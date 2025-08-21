package com.magicscience.magicsciencemod.aspects.cores.collisions;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;

import java.util.Objects;

public class FireCollision {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void collisionTnt(Level level, BlockHitResult blockHitResult, ServerPlayer sender, Objects... objects) {
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

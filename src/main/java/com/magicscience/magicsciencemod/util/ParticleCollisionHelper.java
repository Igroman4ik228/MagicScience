package com.magicscience.magicsciencemod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

public class ParticleCollisionHelper {
    private static final double EPSILON = 0.001D;

    public static @NotNull Vec3 collideWithBlock(
        Vec3 deltaMovement,
        AABB collisionBox,
        @NotNull Level level
    ) {
        AABB expanded = collisionBox.expandTowards(deltaMovement).inflate(EPSILON);

        int minX = (int) Math.floor(expanded.minX);
        int maxX = (int) Math.floor(expanded.maxX);
        int minY = (int) Math.floor(expanded.minY);
        int maxY = (int) Math.floor(expanded.maxY);
        int minZ = (int) Math.floor(expanded.minZ);
        int maxZ = (int) Math.floor(expanded.maxZ);

        double dx = deltaMovement.x;
        double dy = deltaMovement.y;
        double dz = deltaMovement.z;

        Map<BlockState, VoxelShape> shapeCache = new IdentityHashMap<>();

        for (BlockPos pos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;

            if (state.isCollisionShapeFullBlock(level, pos)) {
                AABB blockBox = new AABB(
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1
                );
                dx = collideAxis(collisionBox, dx, 1, 0, 0, blockBox);
                dy = collideAxis(collisionBox, dy, 0, 1, 0, blockBox);
                dz = collideAxis(collisionBox, dz, 0, 0, 1, blockBox);
            }

            // ToDo: collision not full block

        }

        return new Vec3(dx, dy, dz);
    }

    private static double collideAxis(
        AABB collisionBox,
        double movement,
        int axisX, int axisY, int axisZ,
        AABB blockBox
    ) {
        if (movement==0.0D)
            return 0.0D;

        AABB moved = collisionBox.move(axisX * movement, axisY * movement, axisZ * movement);
        if (!moved.intersects(blockBox)) {
            return movement;
        }

        if (movement > 0.0D) {
            if (axisX==1) return Math.min(movement, blockBox.minX - collisionBox.maxX);
            if (axisY==1) return Math.min(movement, blockBox.minY - collisionBox.maxY);
            return Math.min(movement, blockBox.minZ - collisionBox.maxZ);
        } else {
            if (axisX==1) return Math.max(movement, blockBox.maxX - collisionBox.minX);
            if (axisY==1) return Math.max(movement, blockBox.maxY - collisionBox.minY);
            return Math.max(movement, blockBox.maxZ - collisionBox.minZ);
        }
    }
}

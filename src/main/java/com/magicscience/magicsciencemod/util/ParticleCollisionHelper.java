package com.magicscience.magicsciencemod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ParticleCollisionHelper {
    private static final VoxelShape SHAPE_BLOCK = Shapes.block();
//    private static final double EPSILON = 0.001D;

    public static @NotNull Vec3 collideWithBlock(
        @NotNull Vec3 deltaMovement,
        @NotNull AABB collisionBox,
        @NotNull Level level


    ) {
        double dx = deltaMovement.x;
        double dy = deltaMovement.y;
        double dz = deltaMovement.z;

        AABB expanded = collisionBox.expandTowards(deltaMovement);

        //?
        var blockPositions = BlockPos.betweenClosed(
            Mth.floor(expanded.minX),
            Mth.floor(expanded.minY),
            Mth.floor(expanded.minZ),
            Mth.floor(expanded.maxX),
            Mth.floor(expanded.maxY),
            Mth.floor(expanded.maxZ)
        );
//
//        int minX = Mth.floor(expanded.minX - 1.0E-7D) - 1;
//        int maxX = Mth.floor(expanded.maxX + 1.0E-7D) + 1;
//        int minY = Mth.floor(expanded.minY - 1.0E-7D) - 1;
//        int maxY = Mth.floor(expanded.maxY + 1.0E-7D) + 1;
//        int minZ = Mth.floor(expanded.minZ - 1.0E-7D) - 1;
//        int maxZ = Mth.floor(expanded.maxZ + 1.0E-7D) + 1;
//
//        var blockPositions = BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ);

        for (BlockPos blockPos : blockPositions) {
            if (dx==0.0D && dy==0.0D && dz==0.0D)
                break;

            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.isAir() || blockState.is(Blocks.MOVING_PISTON))
                continue;

            if (blockState.isCollisionShapeFullBlock(level, blockPos)) {
//                int bx = blockPos.getX();
//                int by = blockPos.getY();
//                int bz = blockPos.getZ();
//                AABB blockBox = new AABB(bx, by, bz, bx + 1.0D, by + 1.0D, bz + 1.0D);
//                dx = collideAxis(collisionBox, dx, 1, 0, 0, blockBox);
//                dy = collideAxis(collisionBox, dy, 0, 1, 0, blockBox);
//                dz = collideAxis(collisionBox, dz, 0, 0, 1, blockBox);

                VoxelShape blockShape = SHAPE_BLOCK.move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                Vec3 movement = collideWithShape(new Vec3(dx, dy, dz), collisionBox, blockShape);

                dx = movement.x;
                dy = movement.y;
                dz = movement.z;
            } else {
                VoxelShape shape = blockState.getCollisionShape(level, blockPos);

                if (shape.isEmpty())
                    continue;

                VoxelShape movedShape = shape.move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                Vec3 movement = collideWithShape(new Vec3(dx, dy, dz), collisionBox, movedShape);

                dx = movement.x;
                dy = movement.y;
                dz = movement.z;
            }
        }

        return new Vec3(dx, dy, dz);
    }

    private static Vec3 collideWithShape(Vec3 deltaMovement, AABB entityBB, @NotNull VoxelShape shape) {
        if (shape.isEmpty()) {
            return deltaMovement;
        }

        var shapes = List.of(shape);

        double dx = deltaMovement.x;
        double dy = deltaMovement.y;
        double dz = deltaMovement.z;

        if (dy!=0.0D) {
            dy = Shapes.collide(Direction.Axis.Y, entityBB, shapes, dy);
            if (dy!=0.0D) {
                entityBB = entityBB.move(0.0D, dy, 0.0D);
            }
        }

        boolean prioritizeZ = Math.abs(dx) < Math.abs(dz);

        if (prioritizeZ && dz!=0.0D) {
            dz = Shapes.collide(Direction.Axis.Z, entityBB, shapes, dz);
            if (dz!=0.0D) {
                entityBB = entityBB.move(0.0D, 0.0D, dz);
            }
        }

        if (dx!=0.0D) {
            dx = Shapes.collide(Direction.Axis.X, entityBB, shapes, dx);
            if (!prioritizeZ && dx!=0.0D) {
                entityBB = entityBB.move(dx, 0.0D, 0.0D);
            }
        }

        if (!prioritizeZ && dz!=0.0D) {
            dz = Shapes.collide(Direction.Axis.Z, entityBB, shapes, dz);
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
            if (axisX==1)
                return Math.min(movement, blockBox.minX - collisionBox.maxX);
            if (axisY==1)
                return Math.min(movement, blockBox.minY - collisionBox.maxY);
            return Math.min(movement, blockBox.minZ - collisionBox.maxZ);
        } else {
            if (axisX==1)
                return Math.max(movement, blockBox.maxX - collisionBox.minX);
            if (axisY==1)
                return Math.max(movement, blockBox.maxY - collisionBox.minY);
            return Math.max(movement, blockBox.maxZ - collisionBox.minZ);
        }
    }
}

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

import java.util.ArrayList;
import java.util.List;

public class ParticleCollisionHelper {
    private static final VoxelShape SHAPE_FULL_BLOCK = Shapes.block();

    @NotNull
    public static Vec3 collideWithBlock(
        @NotNull Vec3 deltaMovement,
        @NotNull AABB collisionBox,
        @NotNull Level level
    ) {
        List<VoxelShape> potentialShapes = new ArrayList<>();

        AABB expanded = collisionBox.expandTowards(deltaMovement);

        Iterable<BlockPos> blockPositions = BlockPos.betweenClosed(
            Mth.floor(expanded.minX),
            Mth.floor(expanded.minY),
            Mth.floor(expanded.minZ),
            Mth.floor(expanded.maxX),
            Mth.floor(expanded.maxY),
            Mth.floor(expanded.maxZ)
        );

        for (BlockPos pos : blockPositions) {
            BlockState blockState = level.getBlockState(pos);

            if (blockState.isAir() || blockState.is(Blocks.MOVING_PISTON))
                continue;

            VoxelShape shape = SHAPE_FULL_BLOCK;
            if (!blockState.isCollisionShapeFullBlock(level, pos)) {
                shape = blockState.getCollisionShape(level, pos);

                if (shape.isEmpty())
                    continue;
            }

            potentialShapes.add(shape.move(pos.getX(), pos.getY(), pos.getZ()));
        }

        return collideWithShapes(deltaMovement, collisionBox, potentialShapes);
    }

    @NotNull
    private static Vec3 collideWithShapes(@NotNull Vec3 deltaMovement, @NotNull AABB entityBB, @NotNull List<VoxelShape> shapes) {
        if (shapes.isEmpty())
            return deltaMovement;

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
}

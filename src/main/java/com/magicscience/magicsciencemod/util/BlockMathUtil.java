package com.magicscience.magicsciencemod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class BlockMathUtil {
    private static final int BLOCK_SIZE = 1;

    public static Direction getClosestDirection(BlockPos pos, Vec3 center) {
        double[] distances = getDistances(pos, center);
        Direction[] directions = Direction.values();

        double min = distances[0];
        Direction closest = directions[0];

        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < min) {
                min = distances[i];
                closest = Direction.values()[i];
            }
        }

        return closest;
    }

    private static double[] getDistances(BlockPos pos, Vec3 center) {
        double minX = pos.getX();
        double minY = pos.getY();
        double minZ = pos.getZ();
        double maxX = minX + BLOCK_SIZE;
        double maxY = minY + BLOCK_SIZE;
        double maxZ = minZ + BLOCK_SIZE;

        return new double[]{
            Math.abs(center.y - minY), // DOWN
            Math.abs(maxY - center.y), // UP
            Math.abs(center.z - minZ), // NORTH
            Math.abs(maxZ - center.z), // SOUTH
            Math.abs(center.x - minX), // WEST
            Math.abs(maxX - center.x)  // EAST
        };
    }
}

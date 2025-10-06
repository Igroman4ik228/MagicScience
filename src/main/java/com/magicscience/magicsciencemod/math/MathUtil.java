package com.magicscience.magicsciencemod.math;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MathUtil {
    public static double @NotNull [] randomAngles(Random random) {
        double theta = Math.acos(2 * random.nextDouble() - 1); // [0, π]
        double phi = 2 * Math.PI * random.nextDouble();      // [0, 2π)
        return new double[]{theta, phi};
    }

    @NotNull
    public static Vec3 sphericalCoordinatesXYZ(Vec3 basePosition, double r, double theta, double phi) {
        double x = r * Math.sin(theta) * Math.cos(phi);
        double y = r * Math.sin(theta) * Math.sin(phi);
        double z = r * Math.cos(theta);
        return basePosition.add(x, y, z);
    }

    @NotNull
    public static Vec3[] localSystemCoordinatesUpRight(Vec3 lookDir, Vec3 worldUp) {
        // Vector pointing to the right relative to lookDir
        Vec3 right = lookDir.cross(worldUp);
        if (right.lengthSqr() < 1e-6) {
            right = new Vec3(1.0, 0.0, 0.0); // fallback if looking straight up/down
        } else {
            right = right.normalize();
        }

        // Vector pointing up relative to lookDir
        Vec3 up = right.cross(lookDir).normalize();
        return new Vec3[]{right, up};
    }

    public static double[] offsetTwoAxes(Random random, double halfWidth, double halfHeight) {
        double offsetRight = (random.nextDouble() - 0.5) * halfWidth;
        double offsetUp = (random.nextDouble() - 0.5) * halfHeight;
        return new double[]{offsetRight, offsetUp};
    }
}

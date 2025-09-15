package com.magicscience.magicsciencemod.mathutils;

import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    private static final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public static double[] randomAngles() {
        double theta = Math.acos(2 * rnd.nextDouble() - 1); // [0, π]
        double phi = 2 * Math.PI * rnd.nextDouble();      // [0, 2π)
        return new double[]{theta, phi};
    }

    public static Vec3 sphericalCoordinatesXYZ(Vec3 basePosition, double r, double theta, double phi) {
        double x = r * Math.sin(theta) * Math.cos(phi);
        double y = r * Math.sin(theta) * Math.sin(phi);
        double z = r * Math.cos(theta);
        return basePosition.add(x, y, z);
    }

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

    public static double[] offsetTwoAxes(double halfWidth, double halfHeight) {
        double offsetRight = (rnd.nextDouble() - 0.5) * halfWidth;
        double offsetUp = (rnd.nextDouble() - 0.5) * halfHeight;
        return new double[]{offsetRight, offsetUp};
    }
}

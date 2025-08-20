package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class WallStructure extends BaseMagicStructure {
    public WallStructure(int manaCost, int countParticles, int size, int stack) {
        super(manaCost, countParticles, size, stack);
    }

    public WallStructure() {
        this(20, 50, 1, 1);
    }

    public WallStructure(int stack) {
        this(20, 50, 1, stack);
    }

    @Override
    public @NotNull IMagicStructure cloneWithArguments(Object... args) {
        return new WallStructure((int) args[0]);
    }

    @Override
    public @NotNull Vec3 calculateStartParticlePosition(Vec3 basePosition) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return basePosition;

        Vec3 lookDir = mc.player.getLookAngle().normalize();
        Vec3 worldUp = new Vec3(0.0, 1.0, 0.0);     // global "up" axis

        // Vector pointing to the right relative to lookDir
        Vec3 right = lookDir.cross(worldUp);
        if (right.lengthSqr() < 1e-6) {
            right = new Vec3(1.0, 0.0, 0.0); // fallback if looking straight up/down
        } else {
            right = right.normalize();
        }

        // Vector pointing up relative to lookDir
        Vec3 upDir = right.cross(lookDir).normalize();

        final double baseForwardDistance = 1.0;
        final double distancePerSize = 0.4;
        final double baseHalfWidth = 0.5;
        final double widthPerParticle = 0.25;
        final double widthPerSize = 0.3;
        final double baseHalfHeight = 1.2;
        final double heightPerParticle = 0.30;
        final double heightPerSize = 0.5;

        int size = this.getSize();
        int countParticles = this.getCountParticles();

        double forwardDistance = baseForwardDistance + size * distancePerSize;

        double baseSpread = Math.sqrt(Math.max(1, countParticles));
        double halfWidth = baseHalfWidth + baseSpread * widthPerParticle + size * widthPerSize;
        double halfHeight = baseHalfHeight + baseSpread * heightPerParticle + size * heightPerSize;

        var random = ThreadLocalRandom.current();
        double offsetRight = (random.nextDouble() - 0.5) * halfWidth;
        double offsetUp = (random.nextDouble() - 0.5) * halfHeight;

        Vec3 wallCenter = basePosition.add(lookDir.scale(forwardDistance));

        return wallCenter.add(right.scale(offsetRight)).add(upDir.scale(offsetUp));
    }
}

package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class RayStructure extends BaseMagicStructure {
    public RayStructure(BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public RayStructure() {
        this(new BaseStructureData(20, 15, 1), 1);
    }

    public RayStructure(int stack) {
        this(new BaseStructureData(20, 15, 1), stack);
    }

    @Override
    public @NotNull IMagicStructure cloneWithArguments(Object... args) {
        return new RayStructure((int) args[0]);
    }

    @Override
    public @NotNull Vec3 calculateStartParticlePosition(Vec3 basePosition) {

        var random = ThreadLocalRandom.current();
        var mc = Minecraft.getInstance();
        if (mc.player==null) return basePosition;

        final double rayLength = 3.0;
        final double maxOffsetX = 0.10;
        final double maxOffsetY = 0.10;
        final double maxOffsetZ = 0.10;

        Vec3 dir = mc.player.getLookAngle().normalize();

        double distanceAlongBeam = random.nextDouble() * rayLength;

        double xOffset = (random.nextDouble() - 0.5) * maxOffsetX;
        double yOffset = (random.nextDouble() - 0.5) * maxOffsetY;
        double zOffset = (random.nextDouble() - 0.5) * maxOffsetZ;

        Vec3 pointOnBeam = basePosition.add(dir.scale(distanceAlongBeam));
        return pointOnBeam.add(xOffset, yOffset, zOffset);
    }
}

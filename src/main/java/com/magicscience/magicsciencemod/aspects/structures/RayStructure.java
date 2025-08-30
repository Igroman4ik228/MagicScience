package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class RayStructure extends BaseMagicStructure {
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureTypes.RAY);

    public RayStructure(@NotNull BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public RayStructure() {
        this(CONFIG.toData(), 1);
    }

    public RayStructure(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    @NotNull
    public IMagicStructure cloneWithArguments(Object... args) {
        return new RayStructure((int) args[0]);
    }

    @Override
    @NotNull
    public Vec3 calculateStartParticlePosition(Vec3 basePosition) {
        var random = ThreadLocalRandom.current();
        var mc = Minecraft.getInstance();
        if (mc.player==null) return basePosition;

        final double rayLength = 3.0;
        final double maxOffsetX = 0.10;
        final double maxOffsetY = 0.10;
        final double maxOffsetZ = 0.10;

        Vec3 dir = mc.player.getLookAngle().normalize();

        double distanceAlongBeam = random.nextDouble() * rayLength * this.getSize();

        double xOffset = (random.nextDouble() - 0.5) * maxOffsetX;
        double yOffset = (random.nextDouble() - 0.5) * maxOffsetY;
        double zOffset = (random.nextDouble() - 0.5) * maxOffsetZ;

        Vec3 pointOnBeam = basePosition.add(dir.scale(distanceAlongBeam));
        return pointOnBeam.add(xOffset, yOffset, zOffset);
    }
}

package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class RayStructure extends BaseMagicStructure {
    public static final double RAY_LENGTH = 3.0;
    public static final double MAX_OFFSET_X = 0.10;
    public static final double MAX_OFFSET_Y = 0.10;
    public static final double MAX_OFFSET_Z = 0.10;
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
        if (mc.player == null) return basePosition;

        Vec3 dir = mc.player.getLookAngle().normalize();

        double distanceAlongBeam = random.nextDouble() * RAY_LENGTH * this.getSize();

        double xOffset = (random.nextDouble() - 0.5) * MAX_OFFSET_X;
        double yOffset = (random.nextDouble() - 0.5) * MAX_OFFSET_Y;
        double zOffset = (random.nextDouble() - 0.5) * MAX_OFFSET_Z;

        Vec3 pointOnBeam = basePosition.add(dir.scale(distanceAlongBeam));
        return pointOnBeam.add(xOffset, yOffset, zOffset);
    }
}

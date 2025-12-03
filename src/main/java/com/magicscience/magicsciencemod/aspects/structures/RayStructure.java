package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RayStructure extends BaseMagicStructure {
    public static final double RAY_LENGTH = 3.0;
    public static final double MAX_OFFSET_X = 0.10;
    public static final double MAX_OFFSET_Y = 0.10;
    public static final double MAX_OFFSET_Z = 0.10;

    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureType.RAY);

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
    public Vec3 calculateStartParticlePosition(@NotNull StructureContext context) {
        double distanceAlongBeam = context.random().nextDouble() * RAY_LENGTH * this.getSize();

        double xOffset = (context.random().nextDouble() - 0.5) * MAX_OFFSET_X;
        double yOffset = (context.random().nextDouble() - 0.5) * MAX_OFFSET_Y;
        double zOffset = (context.random().nextDouble() - 0.5) * MAX_OFFSET_Z;

        Vec3 pointOnBeam = context.basePosition().add(context.lookAngel().scale(distanceAlongBeam));
        return pointOnBeam.add(xOffset, yOffset, zOffset);
    }
}

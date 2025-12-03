package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import com.magicscience.magicsciencemod.math.MathUtil;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SphereStructure extends BaseMagicStructure {
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureType.SPHERE);

    public SphereStructure(@NotNull BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public SphereStructure() {
        this(CONFIG.toData(), 1);
    }

    public SphereStructure(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    @NotNull
    public IMagicStructure cloneWithArguments(Object... args) {
        return new SphereStructure((int) args[0]);
    }

    @Override
    @NotNull
    public Vec3 calculateStartParticlePosition(@NotNull StructureContext context) {
        double radius = 0.3 * getSize();
        double[] angles = MathUtil.randomAngles(context.random());
        return MathUtil.sphericalCoordinatesXYZ(context.basePosition(), radius, angles[0], angles[1]);
    }
}

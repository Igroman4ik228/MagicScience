package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class SphereStructure extends BaseMagicStructure {
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureTypes.SPHERE);

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
    public Vec3 calculateStartParticlePosition(@NotNull Vec3 basePosition) {
        double radius = 0.3 * getSize();
        var rnd = ThreadLocalRandom.current();

        double theta = Math.acos(2 * rnd.nextDouble() - 1); // [0, π]
        double phi = 2 * Math.PI * rnd.nextDouble();        // [0, 2π)

        double x = radius * Math.sin(theta) * Math.cos(phi);
        double y = radius * Math.sin(theta) * Math.sin(phi);
        double z = radius * Math.cos(theta);

        return basePosition.add(x, y, z);
    }
}

package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class ClotStructure extends BaseMagicStructure {
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureTypes.CLOT);

    public ClotStructure(@NotNull BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public ClotStructure() {
        this(CONFIG.toData(), 1);
    }

    public ClotStructure(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    @NotNull
    public IMagicStructure cloneWithArguments(Object... args) {
        return new ClotStructure((int) args[0]);
    }

    @Override
    @NotNull
    public Vec3 calculateStartParticlePosition(@NotNull Vec3 basePosition) {
        double radius = 0.3 * getSize();
        var rnd = ThreadLocalRandom.current();

        double u = rnd.nextDouble();
        double r = radius * Math.cbrt(u);

        double theta = Math.acos(2 * rnd.nextDouble() - 1);
        double phi = 2 * Math.PI * rnd.nextDouble();

        double x = r * Math.sin(theta) * Math.cos(phi);
        double y = r * Math.sin(theta) * Math.sin(phi);
        double z = r * Math.cos(theta);

        return basePosition.add(x, y, z);
    }
}

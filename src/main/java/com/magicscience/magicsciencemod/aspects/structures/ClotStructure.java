package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import java.util.concurrent.ThreadLocalRandom;

public class ClotStructure extends BaseMagicStructure {
    public ClotStructure(int manaCost, int countParticles, int size, int stack) {
        super(manaCost, countParticles, size, stack);
    }

    public ClotStructure() {
        this(20, 10, 1, 1);
    }

    public ClotStructure(int stack) {
        this(20, 10, 1, stack);
    }

    @Override
    public IMagicStructure cloneWithArguments(Object... args) {
        return new ClotStructure((int) args[0]);
    }

    @Override
    public Vec3 calculateStartParticlePosition(Vec3 basePosition) {
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

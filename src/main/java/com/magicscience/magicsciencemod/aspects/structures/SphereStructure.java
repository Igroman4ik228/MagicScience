package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import java.util.concurrent.ThreadLocalRandom;

public class SphereStructure extends BaseMagicStructure{
    public SphereStructure(int manaCost, int countParticles, int size, int stack) {
        super(manaCost, countParticles, size, stack);
    }

    public SphereStructure() {
        this(30, 30, 3, 1);
    }

    public SphereStructure(int stack) {
        this(30, 30, 3, stack);
    }

    @Override
    public IMagicStructure cloneWithArguments(Object... args) {
        return new SphereStructure((int) args[0]);
    }

    @Override
    public Vec3 calculateStartParticlePosition(Vec3 basePosition) {
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

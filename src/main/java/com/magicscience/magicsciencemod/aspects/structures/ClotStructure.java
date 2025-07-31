package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ClotStructure extends BaseMagicStructure {
    public ClotStructure(int manaCost, int countParticles, int size, int stack) {
        super(manaCost, countParticles, size, stack);
    }

    @Override
    public List<Vec3> calculateStartParticlePositions(Vec3 basePosition) {
        List<Vec3> result = new ArrayList<>(getCountParticles());
        double radius = 0.3 * getSize();
        var rnd = ThreadLocalRandom.current();

        for (int i = 0; i < getCountParticles(); i++) {
            double u = rnd.nextDouble();
            double r = radius * Math.cbrt(u);

            // Случайные уголовые координаты
            double theta = Math.acos(2 * rnd.nextDouble() - 1);    // полярный угол [0, π]
            double phi = 2 * Math.PI * rnd.nextDouble();         // азимут [0, 2π)

            // Перевод в декартовы координаты
            double x = r * Math.sin(theta) * Math.cos(phi);
            double y = r * Math.sin(theta) * Math.sin(phi);
            double z = r * Math.cos(theta);

            result.add(basePosition.add(x, y, z));
        }
        return result;
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
}

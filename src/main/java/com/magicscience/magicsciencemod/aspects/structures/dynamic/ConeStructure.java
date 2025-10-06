package com.magicscience.magicsciencemod.aspects.structures.dynamic;

import com.magicscience.magicsciencemod.aspects.structures.*;
import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ConeStructure extends BaseMagicStructure implements IDynamicMagicStructure {
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureTypes.CONE);

    public ConeStructure(BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public ConeStructure() {
        this(CONFIG.toData(), 1);
    }

    public ConeStructure(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    public @NotNull IMagicStructure cloneWithArguments(Object... args) {
        return new ConeStructure((int) args[0]);
    }

    @Override
    public @NotNull Vec3 calculateStartParticlePosition(@NotNull StructureContext context) {
        return context.basePosition();
    }

    @Override
    public @NotNull Vec3 calculateStartParticleVectors(@NotNull DynamicStructureContext context) {
        Vec3 look = context.lookDirection();
        Vec3 eyePos = context.eyePosition();

        double dist = 5.0 * CONFIG.toData().size();
        Vec3 center = eyePos.add(look.scale(dist));

        Vec3 up = new Vec3(0, 1, 0);
        if (Math.abs(look.dot(up)) > 0.95) {
            up = new Vec3(1, 0, 0);
        }

        Vec3 right = look.cross(up).normalize();
        Vec3 forward = look.cross(right).normalize();

        double radius = CONFIG.toData().size();

        double angle = context.random().nextDouble() * (2 * Math.PI);  // [0, 2π)
        double r = radius * Math.sqrt(context.random().nextDouble()); // равномерное распределение по кругу

        Vec3 randomPoint = center
            .add(right.scale(r * Math.cos(angle)))
            .add(forward.scale(r * Math.sin(angle)));

        Vec3 ray = randomPoint.subtract(eyePos);

        double speed = 0.3;
        return ray.normalize().scale(speed);
    }
}

package com.magicscience.magicsciencemod.aspects.structures.dynamic;

import com.magicscience.magicsciencemod.aspects.structures.*;
import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WaveStructure extends BaseMagicStructure implements IDynamicMagicStructure {
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureTypes.WAVE);

    public WaveStructure(BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public WaveStructure() {
        this(CONFIG.toData(), 1);
    }

    public WaveStructure(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    public @NotNull IMagicStructure cloneWithArguments(Object... args) {
        return new WaveStructure((int) args[0]);
    }

    @Override
    public @NotNull Vec3 calculateStartParticlePosition(@NotNull StructureContext context) {
        double radius = 0.8 * CONFIG.toData().size();

        double angle = context.random().nextDouble() * (2 * Math.PI);     // [0, 2π)
        double offset = -0.05 + context.random().nextDouble() * 0.1;      // [-0.05, 0.05]

        Vec3 basePos = context.basePosition();

        double x = basePos.x + (radius + offset) * Math.cos(angle);
        double y = basePos.y - 0.7;
        double z = basePos.z + (radius + offset) * Math.sin(angle);

        return new Vec3(x, y, z);
    }

    @Override
    public @NotNull Vec3 calculateStartParticleVectors(@NotNull DynamicStructureContext context) {
        Vec3 playerPos = context.centerPosition(); // центр игрока
        Vec3 ray = new Vec3(
            context.startPosition().x - playerPos.x,
            0,
            context.startPosition().z - playerPos.z
        );

        double speed = 0.15;
        return ray.normalize().scale(speed);
    }
}

package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

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
    public @NotNull Vec3 calculateStartParticlePosition(Vec3 basePosition) {
        return basePosition;
    }

    @Override
    public Vec3 calculateStartParticleVectors(Vec3 startPosition, Player player) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        Vec3 look = player.getLookAngle().normalize();
        Vec3 eyePos = player.getEyePosition();

        double dist = 5.0 * CONFIG.toData().size();
        Vec3 center = eyePos.add(look.scale(dist));

        Vec3 up = new Vec3(0, 1, 0);
        if (Math.abs(look.dot(up)) > 0.95) {
            up = new Vec3(1, 0, 0);
        }

        Vec3 right = look.cross(up).normalize();
        Vec3 forward = look.cross(right).normalize();

        double radius = CONFIG.toData().size();
        double angle = rnd.nextDouble(0, 2 * Math.PI);
        double r = radius * Math.sqrt(rnd.nextDouble()); // равномерное распределение по кругу

        Vec3 randomPoint = center
            .add(right.scale(r * Math.cos(angle)))
            .add(forward.scale(r * Math.sin(angle)));

        Vec3 ray = randomPoint.subtract(eyePos);

        double speed = 0.3;
        return ray.normalize().scale(speed);
    }
}

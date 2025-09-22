package com.magicscience.magicsciencemod.aspects.structures.dynamic;

import com.magicscience.magicsciencemod.aspects.structures.BaseMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.BaseStructureData;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

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
    public @NotNull Vec3 calculateStartParticlePosition(@NotNull Vec3 basePosition) {
        double radius = 0.8 * CONFIG.toData().size();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        double angle = rnd.nextDouble(0, 2 * Math.PI);
        double offset = rnd.nextDouble(-0.05, 0.05);

        double x = basePosition.x + (radius + offset) * Math.cos(angle);
        double y = basePosition.y - 0.7;
        double z = basePosition.z + (radius + offset) * Math.sin(angle);

        return new Vec3(x, y, z);
    }

    @Override
    public @NotNull Vec3 calculateStartParticleVectors(Vec3 startPosition, Player player) {
        Vec3 playerPos = player.position().add(0, player.getBbHeight() / 2.0, 0); // центр игрока
        Vec3 ray = new Vec3(
            startPosition.x - playerPos.x,
            0,
            startPosition.z - playerPos.z
        );

        double speed = 0.15;
        return ray.normalize().scale(speed);
    }

}

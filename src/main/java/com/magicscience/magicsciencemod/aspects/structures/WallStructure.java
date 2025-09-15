package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.config.server.structure.IBaseStructureConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import com.magicscience.magicsciencemod.mathutils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WallStructure extends BaseMagicStructure {
    public static final double BASE_FORWARD_DISTANCE = 1.0;
    public static final double BASE_HALF_WIDTH = 0.5;
    public static final double WIDTH_PER_PARTICLE = 0.25;
    public static final double WIDTH_PER_SIZE = 0.3;
    public static final double BASE_HALF_HEIGHT = 1.2;
    public static final double HEIGHT_PER_PARTICLE = 0.30;
    public static final double HEIGHT_PER_SIZE = 0.5;
    private static final IBaseStructureConfig CONFIG = StructureConfig.get(StructureTypes.WALL);

    public WallStructure(@NotNull BaseStructureData baseStructureData, int stack) {
        super(baseStructureData, stack);
    }

    public WallStructure() {
        this(CONFIG.toData(), 1);
    }

    public WallStructure(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    @NotNull
    public IMagicStructure cloneWithArguments(Object... args) {
        return new WallStructure((int) args[0]);
    }

    @Override
    @NotNull
    public Vec3 calculateStartParticlePosition(Vec3 basePosition) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return basePosition;

        Vec3 lookDir = mc.player.getLookAngle().normalize();
        Vec3 worldUp = new Vec3(0.0, 1.0, 0.0);

        Vec3[] basis = MathUtils.localSystemCoordinatesUpRight(lookDir, worldUp);
        Vec3 right = basis[0];
        Vec3 upDir = basis[1];

        int size = this.getSize();
        int countParticles = this.getCountParticles();

        double baseSpread = Math.sqrt(Math.max(1, countParticles));
        double halfWidth = BASE_HALF_WIDTH + baseSpread * WIDTH_PER_PARTICLE + size * WIDTH_PER_SIZE;
        double halfHeight = BASE_HALF_HEIGHT + baseSpread * HEIGHT_PER_PARTICLE + size * HEIGHT_PER_SIZE;

        double[] offsets = MathUtils.offsetTwoAxes(halfWidth, halfHeight);
        double offsetRight = offsets[0];
        double offsetUp = offsets[1];

        Vec3 wallCenter = basePosition.add(lookDir.scale(BASE_FORWARD_DISTANCE));
        return wallCenter.add(right.scale(offsetRight)).add(upDir.scale(offsetUp));
    }
}

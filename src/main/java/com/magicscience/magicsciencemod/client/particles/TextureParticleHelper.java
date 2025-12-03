package com.magicscience.magicsciencemod.client.particles;

import com.magicscience.magicsciencemod.aspects.cores.CoreType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public final class TextureParticleHelper {
    private static final RandomSource RANDOM = RandomSource.create();
    private static final int CORE_COUNT = CoreType.values().length + 1;
    private static final int VARIANTS = 3;
    private static final int TOTAL_FRAMES = CORE_COUNT * VARIANTS;

    @NotNull
    public static TextureAtlasSprite selectSprite(int coreId, @NotNull SpriteSet sprites) {
        int clampedCoreId = clampCoreId(coreId);

        int variant = RANDOM.nextInt(1, VARIANTS + 1); // 1...VARIANTS + 1

        int frame = computeFrame(clampedCoreId, variant);
        return sprites.get(frame, TOTAL_FRAMES);
    }

    private static int clampCoreId(int coreId) {
        return Math.max(1, Math.min(coreId, CORE_COUNT));
    }

    private static int computeFrame(int clampedCoreId, int variant) {
        int coreSpriteIndex = clampedCoreId - 1;

        // Offset core
        int baseSpriteIndex = coreSpriteIndex * VARIANTS;

        // Adding a specific option inside the current core
        return baseSpriteIndex + variant;
    }
}

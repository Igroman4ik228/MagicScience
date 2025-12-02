package com.magicscience.magicsciencemod.client.particles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

public class TextureParticleService {
    private static final RandomSource rnd = RandomSource.create();
    private static final int VARIANTS = 3;

    public static TextureAtlasSprite selectSprite(int coreId, SpriteSet sprites) {
        int coreCount = CoreTypes.values().length + 1;

        int clampedCoreId = Math.max(Math.min(coreId, coreCount), 1);
        int variant = rnd.nextInt(VARIANTS);
        int frame = (clampedCoreId - 1) * VARIANTS + variant + 1;
        int totalFrames = coreCount * VARIANTS;
        return sprites.get(frame, totalFrames);
    }
}

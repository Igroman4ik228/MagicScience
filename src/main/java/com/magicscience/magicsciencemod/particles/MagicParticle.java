package com.magicscience.magicsciencemod.particles;

import com.magicscience.magicsciencemod.aspects.SpellData;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import org.slf4j.Logger;

public class MagicParticle extends TextureSheetParticle {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final SpellData spellData;

    public MagicParticle(
            ClientLevel level,
            double x, double y, double z,
            double xd, double yd, double zd,
            SpriteSet sprites,
            SpellData spellData) {
        super(level, x, y, z);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = spellData.particleLifeTime();

        this.spellData = spellData;


        int coreIndex = spellData.coreId() - 1;

        // Всего картинок
        int frameCount = 3;

        // рассчитываем возраст, дающий нужный кадр ПИЗДЕЦ:
        int ageForSprite = coreIndex * this.lifetime / (frameCount - 1);
        TextureAtlasSprite sprite = sprites.get(ageForSprite, this.lifetime);

        this.setSprite(sprite);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    private void playExtinguishSound(Vec3 pos) {
        this.level.playLocalSound(pos.x, pos.y, pos.z,
                SoundEvents.FIRE_EXTINGUISH,
                SoundSource.BLOCKS,
                0.5F,  // громкость
                1.0F,  // питч
                false);
    }
}

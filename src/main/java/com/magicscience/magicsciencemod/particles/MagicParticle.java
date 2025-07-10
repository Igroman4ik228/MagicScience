package com.magicscience.magicsciencemod.particles;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.particles.aspecthandlers.AspectProcessor;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class MagicParticle extends TextureSheetParticle {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int FRAME_COUNT = 3;

    private final @NotNull SpellData spellData;

    private final @NotNull AspectProcessor aspectProcessor;

    public MagicParticle(
        ClientLevel level,
        double x, double y, double z,
        double xd, double yd, double zd,
        SpriteSet sprites,
        SpellData spellData
    ) {
        super(level, x, y, z);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = spellData.particleLifeTime();
        this.spellData = spellData;

        // В будущем может быть усложнение взятия индекса спрайта
        int spriteIndex = Math.max(spellData.coreId() - 1, 0);
        this.setSprite(
            selectSprite(
                spriteIndex,
                lifetime,
                sprites
            )
        );

        LOGGER.info("MagicParticle");
        LOGGER.info("spellData: {}", spellData);

        this.aspectProcessor = new AspectProcessor(this);
    }

    @NotNull
    private static TextureAtlasSprite selectSprite(
        int index,
        int lifetime,
        @NotNull SpriteSet sprites
    ) {
        int ageForSprite = index * lifetime / (FRAME_COUNT - 1);
        return sprites.get(ageForSprite, lifetime);
    }

    @Override
    public void tick() {
        this.aspectProcessor.process();

        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public @NotNull ClientLevel getLevel() {
        return level;
    }

    public @NotNull SpellData getSpellData() {
        return spellData;
    }

    public @NotNull Vec3 getDirectionPos() {
        return new Vec3(xd, yd, zd);
    }
}

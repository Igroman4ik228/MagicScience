package com.magicscience.magicsciencemod.particles;

import com.magicscience.magicsciencemod.aspects.SpellData;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.particles.aspecthandlers.AspectProcessor;
import com.magicscience.magicsciencemod.particles.aspecthandlers.CoreHandler;
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

    private final SpellData spellData;
    private final IMagicCore magicCore;

    private final AspectProcessor aspectProcessor;

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

        this.magicCore = CoreHandler.handle(spellData);

        // В будущем может быть усложнение взятия индекса спрайта
        int spriteIndex = Math.max(spellData.coreId() - 1, 0);
        this.setSprite(
            selectSprite(
                spriteIndex,
                this.lifetime,
                sprites
            )
        );

        LOGGER.info("MagicParticle");
        LOGGER.info("spellData: " + spellData);

        this.aspectProcessor = new AspectProcessor(this);
    }

    private static TextureAtlasSprite selectSprite(int index, int lifetime, SpriteSet sprites) {
        int ageForSprite = index * lifetime / (FRAME_COUNT - 1);
        return sprites.get(ageForSprite, lifetime);
    }

    @Override
    public void tick() {
        // Logic
        this.aspectProcessor.processing(magicCore);

        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public ClientLevel getLevel() {
        return level;
    }

    public SpellData getSpellData() {
        return spellData;
    }

    public Vec3 getDirectionPos() {
        return new Vec3(xd, yd, zd);
    }
}

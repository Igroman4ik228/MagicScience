package com.magicscience.magicsciencemod.client.particles;

import com.magicscience.magicsciencemod.aspects.attributes.GravityAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.AspectProcessor;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class MagicParticle extends TextureSheetParticle {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int FRAME_COUNT = 3;
    private final @NotNull UUID particleUUID;
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

        this.setSize(0.1f, 0.1f);

        // ToDo: В будущем может быть усложнение взятия индекса спрайта
        int spriteIndex = Math.max(spellData.coreId() - 1, 0);
        this.setSprite(
            selectSprite(
                spriteIndex,
                lifetime,
                sprites
            )
        );

        initGravity();

        this.particleUUID = UUID.randomUUID();
        ParticleRegistry.register(this);

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

    private void initGravity() {
        var spell = SpellConverter.toSpell(spellData);
        var gravityAttribute = spell.getMagicAttributes(GravityAttribute.class);
        if (gravityAttribute!=null) {
            this.gravity = gravityAttribute.getGravity();
        }
    }

    @Override
    public void tick() {
        this.aspectProcessor.process();

        super.tick();
    }

    @Override
    public void remove() {
        ParticleRegistry.unregister(this.particleUUID);

        super.remove();
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @NotNull
    public ClientLevel getLevel() {
        return level;
    }

    @NotNull
    public SpellData getSpellData() {
        return spellData;
    }

    @NotNull
    public UUID getParticleUUID() {
        return particleUUID;
    }

    @NotNull
    public Vec3 getDirectionPos() {
        return new Vec3(xd, yd, zd);
    }
}

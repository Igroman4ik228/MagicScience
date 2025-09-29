package com.magicscience.magicsciencemod.client.particles;

import com.magicscience.magicsciencemod.aspects.attributes.GravityAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.AspectProcessor;
import com.magicscience.magicsciencemod.util.ParticleCollisionHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class MagicParticle extends TextureSheetParticle {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int FRAME_COUNT = 3;
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    private static final float COLLISION_EPSILON = 1.0E-5F;

    private final @NotNull UUID particleUUID;
    private final @NotNull SpellData spellData;
    private final @NotNull AspectProcessor aspectProcessor;

    private boolean stoppedByCollision;

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
        // ? Every 4 ticks
//        if (this.age % 4==0) {
//            this.aspectProcessor.process();
//        }
        this.aspectProcessor.process();

        super.tick();
    }

    @Override
    public void move(double dx, double dy, double dz) {
        if (stoppedByCollision)
            return;

        double originalDx = dx;
        double originalDy = dy;
        double originalDz = dz;

        boolean hasMovement = (dx!=0 || dy!=0 || dz!=0);

        if (this.hasPhysics &&
            hasMovement &&
            dx * dx + dy * dy + dz * dz < MAXIMUM_COLLISION_VELOCITY_SQUARED
        ) {
            Vec3 vec = ParticleCollisionHelper.collideWithBlock(
                new Vec3(dx, dy, dz),
                this.getBoundingBox(),
                this.level
            );

            dx = vec.x;
            dy = vec.y;
            dz = vec.z;

            hasMovement = (dx!=0 || dy!=0 || dz!=0);
        }

        if (hasMovement) {
            this.setBoundingBox(this.getBoundingBox().move(dx, dy, dz));
            this.setLocationFromBoundingbox();
        }

        if (Math.abs(originalDy) >= (double) COLLISION_EPSILON && Math.abs(dy) < (double) COLLISION_EPSILON) {
            this.stoppedByCollision = true;
        }

        this.onGround = originalDy!=dy && originalDy < 0.0D;

        if (originalDx!=dx) {
            this.xd = 0.0D;
        }
        if (originalDz!=dz) {
            this.zd = 0.0D;
        }
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

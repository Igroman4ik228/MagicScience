package com.magicscience.magicsciencemod.client.particles;

import com.magicscience.magicsciencemod.aspects.attributes.GravityAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.AspectProcessor;
import com.magicscience.magicsciencemod.util.ParticleCollisionHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class MagicParticle extends TextureSheetParticle {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    private static final float COLLISION_EPSILON = 1.0E-5F;

    private final @NotNull UUID particleUUID = UUID.randomUUID();
    private final @NotNull SpellData spellData;
    private final @NotNull AspectProcessor aspectProcessor;
    private @NotNull UUID observerClientUUID;

    private boolean stoppedByCollision;

    public MagicParticle(
        @NotNull ClientLevel level,
        double x, double y, double z,
        double xd, double yd, double zd,
        @NotNull SpriteSet sprites,
        @NotNull SpellData spellData
    ) {
        super(level, x, y, z);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.spellData = spellData;
        this.lifetime = spellData.particleLifeTime();
        this.observerClientUUID = spellData.ownerUUID();

        this.setSize(0.1f, 0.1f);

        this.setSprite(
            TextureParticleHelper.selectSprite(
                spellData.coreId(),
                sprites
            )
        );

        initGravity();

        ParticleRegistry.register(this);

        this.aspectProcessor = new AspectProcessor(this);
    }

    public static boolean hasMovement(double xd, double yd, double zd) {
        return xd!=0.0D || yd!=0.0D || zd!=0.0D;
    }

    private void initGravity() {
        var spell = SpellConverter.toSpell(spellData);
        var gravityAttribute = spell.getMagicAttribute(GravityAttribute.class);
        if (gravityAttribute!=null) {
            this.gravity = gravityAttribute.getGravity();
        }
    }

    @Override
    public void tick() {
        observerProcessing();

        super.tick();
    }

    private void observerProcessing() {
        var mc = Minecraft.getInstance();
        if (mc.player==null) return;

        UUID clientUUID = mc.player.getUUID();

        if (clientUUID.equals(this.observerClientUUID)) {
            aspectProcessor.process();
        }
    }

    public @NotNull UUID getObserverClientUUID() {
        return this.observerClientUUID;
    }

    public void setObserverClientUUID(@NotNull UUID newObserver) {
        LOGGER.info("setObserverClientUUID {} -> {}", this.observerClientUUID, newObserver);

        this.observerClientUUID = newObserver;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        if (this.stoppedByCollision)
            return;

        double originalDx = dx;
        double originalDy = dy;
        double originalDz = dz;

        // Check and handle potential block collisions
        if (this.shouldAttemptCollision(dx, dy, dz)) {
            Vec3 collided = ParticleCollisionHelper.collideWithBlock(
                new Vec3(dx, dy, dz),
                this.getBoundingBox(),
                this.level
            );

            dx = collided.x;
            dy = collided.y;
            dz = collided.z;
        }

        // Apply movement if the particle has any motion
        applyMovement(dx, dy, dz);

        // Update particle state after movement
        // (collision stop detection, ground check, velocity reset)
        updateCollisionState(originalDx, originalDy, originalDz, dx, dy, dz);
    }

    private void applyMovement(double dx, double dy, double dz) {
        if (hasMovement(dx, dy, dz)) {
            this.setBoundingBox(this.getBoundingBox().move(dx, dy, dz));
            this.setLocationFromBoundingbox();
        }
    }

    private void updateCollisionState(
        double originalDx, double originalDy, double originalDz,
        double newDx, double newDy, double newDz
    ) {
        checkCollisionStop(originalDy, newDy);

        this.onGround = originalDy!=newDy && originalDy < 0.0D;
        if (originalDx!=newDx)
            this.xd = 0.0D;
        if (originalDz!=newDz)
            this.zd = 0.0D;
    }

    private void checkCollisionStop(double originalDy, double newDy) {
        boolean hadVerticalMovement = Math.abs(originalDy) >= COLLISION_EPSILON;
        boolean lostVerticalMovement = Math.abs(newDy) < COLLISION_EPSILON;

        if (hadVerticalMovement && lostVerticalMovement) {
            this.stoppedByCollision = true;
        }
    }

    private boolean shouldAttemptCollision(double dx, double dy, double dz) {
        if (!hasPhysics || !hasMovement(dx, dy, dz)) {
            return false;
        }

        double squaredLength = dx * dx + dy * dy + dz * dz;
        return squaredLength < MAXIMUM_COLLISION_VELOCITY_SQUARED;
    }

    public boolean hasMovement() {
        return hasMovement(this.xd, this.yd, this.zd);
    }

    @Override
    public void remove() {
        ParticleRegistry.unregister(this.particleUUID);

        super.remove();
    }

    @Override
    public int getLightColor(float partialTick) {
        // Max brightness
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

    public boolean isStoppedByCollision() {
        return stoppedByCollision;
    }

    @NotNull
    public Vec3 getDirectionVec() {
        return new Vec3(xd, yd, zd);
    }
}

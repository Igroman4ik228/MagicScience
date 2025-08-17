package com.magicscience.magicsciencemod.mixins;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TrackingEmitter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
@OnlyIn(Dist.CLIENT)
public class ParticleEngineMixin {
    @Shadow
    protected ClientLevel level;
    @Final
    @Shadow
    private Map<ParticleRenderType, Queue<Particle>> particles;
    @Final
    @Shadow
    private Queue<TrackingEmitter> trackingEmitters;
    @Final
    @Shadow
    private Queue<Particle> particlesToAdd;

    @Shadow
    private void tickParticleList(Collection<Particle> pParticles) {
    }

    /**
     * Limit particles -> 100_000
     *
     * @author Igroman4ik
     * @reason Combability for optimized mods
     */
    @Overwrite
    public void tick() {
        this.particles.forEach((p_288249_, p_288250_) -> {
            this.level.getProfiler().push(p_288249_.toString());
            this.tickParticleList(p_288250_);
            this.level.getProfiler().pop();
        });

        if (!this.trackingEmitters.isEmpty()) {
            List<TrackingEmitter> list = Lists.newArrayList();

            for (TrackingEmitter trackingemitter : this.trackingEmitters) {
                trackingemitter.tick();
                if (!trackingemitter.isAlive()) {
                    list.add(trackingemitter);
                }
            }

            this.trackingEmitters.removeAll(list);
        }

        Particle particle;
        if (!this.particlesToAdd.isEmpty()) {
            while ((particle = this.particlesToAdd.poll())!=null) {
                this.particles.computeIfAbsent(particle.getRenderType(), (p_107347_) -> {
                    // изменено: 100_000 вместо 16_384
                    return EvictingQueue.create(100_000);
                }).add(particle);
            }
        }
    }
}
package com.magicscience.magicsciencemod.mixins;

import com.mojang.logging.LogUtils;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class AdjusterMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PARTICLE_ENGINE_MIXIN = "com.magicscience.magicsciencemod.mixins.ParticleEngineMixin";
    private static final String ASYNC_PARTICLES_MOD_CLASS = "forge.fun.qu_an.minecraft.asyncparticles.client.AsyncParticlesClient";

    private static boolean asyncParticlesPresent = false;

    @Override
    public void onLoad(String mixinPackage) {
        asyncParticlesPresent = isClassPresent(ASYNC_PARTICLES_MOD_CLASS);
    }

    private boolean isClassPresent(String className) {
        try {
            Class.forName(className, false, this.getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (PARTICLE_ENGINE_MIXIN.equals(mixinClassName)) {
            if (asyncParticlesPresent) {
                LOGGER.debug("Skipping ParticleEngineMixin due to asyncparticles mod presence");
                return false;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}


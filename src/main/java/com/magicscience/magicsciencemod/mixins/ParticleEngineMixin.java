package com.magicscience.magicsciencemod.mixins;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ParticleEngine.class)
@OnlyIn(Dist.CLIENT)
public class ParticleEngineMixin {
    @ModifyConstant(
        method = "lambda$tick$11",
        constant = @Constant(intValue = 16384)
    )
    private static int magicScience$modifyParticleLimit(int original) {
        return 100_000;
    }
}
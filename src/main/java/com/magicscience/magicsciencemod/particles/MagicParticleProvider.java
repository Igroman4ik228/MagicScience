package com.magicscience.magicsciencemod.particles;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MagicParticleProvider implements ParticleProvider<MagicParticleOptions> {
    private final SpriteSet spriteSet;

    public MagicParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(
        @NotNull MagicParticleOptions data,
        @NotNull ClientLevel level,
        double x, double y, double z,
        double xd, double yd, double zd
    ) {
        var spellData = new SpellData(
            data.ownerId(),
            data.coreId(),
            data.attributeIds(),
            data.structureId(),
            data.particleSpeed(),
            data.particleLifeTime()
        );

        return new MagicParticle(level, x, y, z, xd, yd, zd, spriteSet, spellData);
    }
}
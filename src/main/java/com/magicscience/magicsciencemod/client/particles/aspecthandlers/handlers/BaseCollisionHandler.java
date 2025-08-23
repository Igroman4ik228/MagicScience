package com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public abstract class BaseCollisionHandler<F> implements ICollisionHandler {
    protected final MagicParticle particle;
    protected final SpellData spellData;
    protected final Predicate<F> filter;
    protected final Level level;

    public BaseCollisionHandler(MagicParticle particle, SpellData spellData, Predicate<F> filter, Level level) {
        this.particle = particle;
        this.spellData = spellData;
        this.filter = filter;
        this.level = level;
    }
}

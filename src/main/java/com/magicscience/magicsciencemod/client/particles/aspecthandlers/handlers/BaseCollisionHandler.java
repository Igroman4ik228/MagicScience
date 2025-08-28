package com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class BaseCollisionHandler<F> implements ICollisionHandler {
    protected final MagicParticle particle;
    protected final SpellData spellData;
    protected final Predicate<F> filter;
    protected final ClientLevel level;

    public BaseCollisionHandler(
        @NotNull MagicParticle particle,
        @NotNull SpellData spellData,
        @NotNull Predicate<F> filter,
        @NotNull ClientLevel level
    ) {
        this.particle = particle;
        this.spellData = spellData;
        this.filter = filter;
        this.level = level;
    }
}

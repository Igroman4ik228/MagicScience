package com.magicscience.magicsciencemod.aspects.cores.effects;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMagicEffect implements IMagicEffect {
    @Override
    public final void applyEffect(@NotNull Entity entity) {
        // Base effects/checks/before or after
        if (!canApply(entity))
            return;

        beforeApply(entity);

        apply(entity);

        afterApply(entity);
    }

    protected abstract void apply(@NotNull Entity entity);

    protected boolean canApply(@NotNull Entity entity) {
        return entity.isAlive() && !entity.isInvulnerable();
    }

    protected void beforeApply(@NotNull Entity entity) {
    }

    protected void afterApply(@NotNull Entity entity) {
    }
}

package com.magicscience.magicsciencemod.aspects.cores.effects;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class BornEffect extends BaseMagicEffect {
    private final int burnDuration;

    public BornEffect(int burnDuration) {
        this.burnDuration = burnDuration;
    }

    @Override
    protected void apply(@NotNull Entity entity) {
        entity.setSecondsOnFire(burnDuration);
    }
}

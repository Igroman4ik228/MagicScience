package com.magicscience.magicsciencemod.aspects.cores.effects;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface IMagicEffect {
    void applyEffect(@NotNull Entity entity);
}

package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public interface IMagicCore extends IMagicAspect {
    int getDamage();

    int getParticleLifeTime();

    int getStack();

    IMagicCore cloneWithArguments(Object... args);

    void processingBlock(BlockHitResult blockHitResult, Player sender, Objects... objects);

    @NotNull Collection<IMagicEffect> getMagicEffects();
}

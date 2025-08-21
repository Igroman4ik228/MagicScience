package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public interface IMagicCore extends IMagicAspect {
    int getDamage();

    int getParticleLifeTime();

    int getStack();

    IMagicCore cloneWithArguments(Object... args);

    void processingBlock(Block block, Level level, BlockHitResult blockHitResult, ServerPlayer sender, Objects... objects);

    @NotNull Collection<IMagicEffect> getMagicEffects();
}

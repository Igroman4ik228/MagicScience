package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BaseMagicCore implements IMagicCore {
    protected final HashMap<Class<? extends Block>, IAction> blockActionMap;
    protected final HashMap<Predicate<Class<? extends Block>>, IAction> groupBlockActionMap;
    private final @NotNull BaseCoreData baseCoreData;
    private final int stack;
    private final @NotNull Collection<IMagicEffect> effects;

    public BaseMagicCore(
        @NotNull BaseCoreData baseCoreData,
        int stack,
        @NotNull Collection<IMagicEffect> effects
    ) {
        this.baseCoreData = baseCoreData;
        this.stack = Math.max(1, stack);
        this.effects = effects;

        this.blockActionMap = new HashMap<Class<? extends Block>, IAction>();
        this.groupBlockActionMap = new HashMap<Predicate<Class<? extends Block>>, IAction>();
    }

    @Override
    public int getManaCost() {
        return baseCoreData.manaCost() * stack;
    }

    @Override
    public int getDamage() {
        return baseCoreData.damage() * stack;
    }

    @Override
    public int getParticleLifeTime() {
        return baseCoreData.particleLifeTime();
    }

    @Override
    public int getParticleCount() {
        return baseCoreData.particleCount();
    }

    @Override
    public float getSize() {
        return baseCoreData.size() * stack;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public void processingBlock(BlockHitResult blockHitResult,
                                Player sender, Objects... objects) {
        var level = sender.level();
        var blockPos = blockHitResult.getBlockPos();
        var block = level.getBlockState(blockPos).getBlock();
        var blockClass = block.getClass();
        var action = this.blockActionMap.get(blockClass);

        if (action==null) {
            action = this.groupBlockActionMap.get(blockClass);
            if (action==null) {
                commonProcessingBlock(blockHitResult, sender, objects);
            }
        }

        action.execute(blockHitResult, sender, objects);
    }

    protected abstract void commonProcessingBlock(BlockHitResult blockHitResult,
                                                  Player sender, Objects... objects);

    @Override
    @NotNull
    public Collection<IMagicEffect> getMagicEffects() {
        return effects;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null) return false;
        return this.getClass()==o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

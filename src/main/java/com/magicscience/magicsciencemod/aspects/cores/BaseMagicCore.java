package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.IAction;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
    private final int manaCost;
    private final int damage;
    private final int particleLifeTime;
    private final int stack;
    private final @NotNull Collection<IMagicEffect> effects;

    public BaseMagicCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int stack,
        @NotNull Collection<IMagicEffect> effects
    ) {
        this.manaCost = manaCost;
        this.damage = damage;
        this.particleLifeTime = particleLifeTime;
        this.stack = Math.max(1, stack);
        this.effects = effects;

        this.blockActionMap = new HashMap<Class<? extends Block>, IAction>();
        this.groupBlockActionMap = new HashMap<Predicate<Class<? extends Block>>, IAction>();
    }

    @Override
    public int getManaCost() {
        return manaCost * stack;
    }

    @Override
    public int getDamage() {
        return damage * stack;
    }

    @Override
    public int getParticleLifeTime() {
        return particleLifeTime;
    }

    @Override
    public int getStack() {
        return stack;
    }

    @Override
    public void processingBlock(Block block, Level level, BlockHitResult blockHitResult,
                                ServerPlayer sender, Objects... objects) {
        var blockClass = block.getClass();
        var action = this.blockActionMap.get(blockClass);

        if (action==null) {
            action = this.groupBlockActionMap.get(blockClass);
            if (action==null) {
                commonProcessingBlock(block, level, blockHitResult, sender, objects);
            }
        }

        action.execute(level, blockHitResult, sender, objects);
    }

    protected abstract void commonProcessingBlock(Block block, Level level, BlockHitResult blockHitResult,
                                                  ServerPlayer sender, Objects... objects);

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

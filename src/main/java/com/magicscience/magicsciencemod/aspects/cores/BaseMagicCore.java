package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.collisions.IActions.IActionBlock;
import com.magicscience.magicsciencemod.aspects.cores.collisions.IActions.IActionEntity;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BaseMagicCore implements IMagicCore {
    protected final @NotNull Map<Class<? extends Block>, IActionBlock> blockActions = new HashMap<>();
    protected final @NotNull Map<Class<? extends Entity>, IActionEntity> entityActions = new HashMap<>();

    protected final @NotNull Map<Predicate<Class<? extends Block>>, IActionBlock> groupBlockActions = new HashMap<>();
    protected final @NotNull Map<Predicate<? extends Entity>, IActionEntity> groupEntityActions = new HashMap<>();

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
    public int getStack() {
        return stack;
    }

    @Override
    public void processingBlock(
        @NotNull BlockHitResult blockHitResult,
        @NotNull ServerPlayer sender,
        Objects... objects
    ) {
        var level = sender.serverLevel();
        var blockPos = blockHitResult.getBlockPos();
        var block = level.getBlockState(blockPos).getBlock();
        var blockClass = block.getClass();

        var action = this.blockActions.get(blockClass);

        if (action == null) {
            action = this.groupBlockActions.get(blockClass);
            if (action == null) {
                commonProcessingBlock(blockHitResult, sender, objects);
            }
        }

        if (action == null) return;
        action.execute(blockHitResult, sender, objects);
    }

    protected abstract void commonProcessingBlock(
        @NotNull BlockHitResult blockHitResult,
        @NotNull ServerPlayer sender,
        Objects... objects
    );

    @Override
    public void processingEntity(
        @NotNull Entity entity,
        @NotNull ServerPlayer sender,
        Objects... objects
    ) {
        var entityClass = entity.getClass();

        var action = this.entityActions.get(entityClass);

        if (action == null) {
            action = this.groupEntityActions.get(entityClass);
            if (action == null) {
                commonProcessingEntity(entity, sender, objects);
            }
        }

        if (action == null) return;
        action.execute(entity, sender, objects);
    }

    protected abstract void commonProcessingEntity(
        @NotNull Entity entity,
        @NotNull ServerPlayer sender,
        Objects... objects
    );

    @Override
    @NotNull
    public Collection<IMagicEffect> getMagicEffects() {
        return effects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

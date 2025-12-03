package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.collisions.IActions.IActionBlock;
import com.magicscience.magicsciencemod.aspects.cores.collisions.IActions.IActionEntity;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BaseMagicCore implements IMagicCore {
    private static final Logger LOGGER = LogUtils.getLogger();

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

        var blockAction = this.blockActions.get(blockClass);
        if (blockAction!=null) {
            blockAction.execute(blockHitResult, sender, objects);
        }

        var groupAction = this.groupBlockActions.get(blockClass);
        if (groupAction!=null) {
            groupAction.execute(blockHitResult, sender, objects);
        }

        LOGGER.info("!!!");

        if (blockAction==null && groupAction==null) {
            LOGGER.info("###");
            commonProcessingBlock(blockHitResult, sender, objects);
        }
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

        var entityAction = this.entityActions.get(entityClass);
        if (entityAction!=null) {
            entityAction.execute(entity, sender, objects);
        }

        var groupAction = this.groupEntityActions.get(entityClass);
        if (groupAction!=null) {
            groupAction.execute(entity, sender, objects);
        }

        if (entityAction==null && groupAction==null) {
            commonProcessingEntity(entity, sender, objects);
        }
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
}

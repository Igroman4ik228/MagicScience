package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.collisions.WaterCollision;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.config.server.core.BaseCoreConfig;
import com.magicscience.magicsciencemod.config.server.core.CoreConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class WaterCore extends BaseMagicCore {
    private static final BaseCoreConfig CONFIG = (BaseCoreConfig) CoreConfig.get(CoreType.WATER);
    private static final Logger LOGGER = LogUtils.getLogger();

    public WaterCore(
        @NotNull BaseCoreData baseCoreData,
        int stack,
        @NotNull Collection<IMagicEffect> effects
    ) {
        super(baseCoreData, stack, effects);
        fillBlockActionMaps();
    }

    public WaterCore() {
        this(CONFIG.toData(), 1, List.of());
    }

    public WaterCore(int stack) {
        this(CONFIG.toData(), stack, List.of());
    }

    private void fillBlockActionMaps() {
        this.blockActions.put(Blocks.FIRE.getClass(), WaterCollision::collisionFire);
    }

    @Override
    protected void commonProcessingBlock(
        @NotNull BlockHitResult blockHitResult,
        @NotNull ServerPlayer sender,
        Objects... objects
    ) {
    }

    @Override
    protected void commonProcessingEntity(
        @NotNull Entity entity,
        @NotNull ServerPlayer sender,
        Objects... objects
    ) {
    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new WaterCore((int) args[0]);
    }
}

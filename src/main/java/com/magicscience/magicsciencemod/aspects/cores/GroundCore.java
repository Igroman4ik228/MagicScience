package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.config.server.core.CoreConfig;
import com.magicscience.magicsciencemod.config.server.core.GroundCoreConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class GroundCore extends BaseMagicCore {
    private static final Logger LOGGER = LogUtils.getLogger();


    private static final GroundCoreConfig CONFIG = (GroundCoreConfig) CoreConfig.get(CoreTypes.GROUND);

    public GroundCore(
            @NotNull BaseCoreData baseCoreData,
            int stack,
            @NotNull Collection<IMagicEffect> effects
    ) {
        super(baseCoreData, stack, effects);
        fillBlockActionMaps();
    }

    public GroundCore() {
        this(CONFIG.toData(), 1, List.of());
    }

    public GroundCore(int stack) {
        this(CONFIG.toData(), stack, List.of());
    }

    private void fillBlockActionMaps() {

    }

    @Override
    protected void commonProcessingBlock(@NotNull BlockHitResult blockHitResult, @NotNull ServerPlayer sender, Objects... objects) {
        var level = sender.level();
        var pos = blockHitResult.getBlockPos();

        LOGGER.info("!");

        level.destroyBlock(pos, false, sender);
    }

    @Override
    protected void commonProcessingEntity(@NotNull Entity entity, @NotNull ServerPlayer sender, Objects... objects) {

    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new GroundCore((int) args[0]);
    }
}

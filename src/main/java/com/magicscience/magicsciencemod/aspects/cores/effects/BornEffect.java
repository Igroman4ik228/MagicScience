package com.magicscience.magicsciencemod.aspects.cores.effects;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class BornEffect extends BaseMagicEffect {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    protected void apply(@NotNull Entity entity) {
        entity.setSecondsOnFire(5);
        LOGGER.info("doApply: BornEffect");
    }
}

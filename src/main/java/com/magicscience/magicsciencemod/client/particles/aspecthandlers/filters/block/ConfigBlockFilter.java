package com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block;

import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.function.Predicate;

public class ConfigBlockFilter implements Predicate<BlockState> {
    // ToDo: Вынести в конфиг
    private static final Set<Class<?>> EXCLUDED_BLOCKS = Set.of();

    @Override
    public boolean test(BlockState blockState) {
        return EXCLUDED_BLOCKS.stream()
            .noneMatch(cls -> cls.isInstance(blockState.getBlock()));
    }
}

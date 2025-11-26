package com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class CoreBlockFilter implements Predicate<BlockState> {
    private static final Map<Integer, Set<Class<? extends Block>>> EXCLUDED_BLOCKS_BY_CORE = Map.of(
        CoreTypes.FIRE.getId(), Set.of(
            Blocks.TNT.getClass(),
            Blocks.WATER.getClass()
        ),
        CoreTypes.WATER.getId(), Set.of(
            Blocks.FIRE.getClass()
        )
    );

    private final @NotNull Set<Class<? extends Block>> blockClasses;

    public CoreBlockFilter(int coreId) {
        this.blockClasses = EXCLUDED_BLOCKS_BY_CORE.getOrDefault(coreId, Set.of());
    }

    @Override
    public boolean test(BlockState blockState) {
        return blockClasses.stream()
            .noneMatch(
                cls -> cls.isInstance(blockState.getBlock())
                    || blockState.isFlammable(null, null, null)
            );
    }
}

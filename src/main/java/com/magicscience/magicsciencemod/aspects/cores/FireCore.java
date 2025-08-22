package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.collisions.FireCollision;
import com.magicscience.magicsciencemod.aspects.cores.effects.BornEffect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FireCore extends BaseMagicCore {
    private static final FireCoreConfig CONFIG = (FireCoreConfig) CoreConfig.get(CoreTypes.FIRE);

    private final int burnDuration;

    public FireCore(
        @NotNull BaseCoreData baseCoreData,
        int stack,
        int burnDuration,
        @NotNull Collection<IMagicEffect> effects
    ) {
        super(baseCoreData, stack, effects);
        fillBlockActionMaps();
        this.burnDuration = burnDuration;
    }

    public FireCore() {
        this(CONFIG.toData(), 1, CONFIG.getBurnDuration(), List.of(new BornEffect(CONFIG.getBurnDuration())));
    }

    public FireCore(int stack) {
        this(CONFIG.toData(), stack, CONFIG.getBurnDuration(), List.of(new BornEffect(CONFIG.getBurnDuration())));
    }

    private void fillBlockActionMaps() {
        this.blockActionMap.put(TntBlock.class, FireCollision::collisionTnt);
    }

    @Override
    protected void commonProcessingBlock(BlockHitResult blockHitResult,
                                         Player sender, Objects... objects) {
        var level = sender.level();
        var blockPos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(blockPos);

        if (state.isFlammable(level, blockPos, blockHitResult.getDirection())) {
            var abovePos = blockPos.relative(blockHitResult.getDirection());

            if (level.getBlockState(abovePos).isAir()) {
                level.setBlockAndUpdate(abovePos, Blocks.FIRE.defaultBlockState());
            }
        }
    }

    public int getBurnDuration() {
        return burnDuration;
    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new FireCore((int) args[0]);
    }
}

package com.magicscience.magicsciencemod.aspects.cores;

import com.magicscience.magicsciencemod.aspects.cores.collisions.FireCollision;
import com.magicscience.magicsciencemod.aspects.cores.effects.BornEffect;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FireCore extends BaseMagicCore {
    private static final int DEFAULT_MANA_COST = 10;
    private static final int DEFAULT_DAMAGE = 10;
    private static final int DEFAULT_PARTICLE_LIFE_TIME = 300;
    private static final int DEFAULT_STACK = 1;
    private static final Collection<IMagicEffect> DEFAULT_EFFECTS = List.of(new BornEffect());

    public FireCore(
        int manaCost,
        int damage,
        int particleLifeTime,
        int particleCount,
        @NotNull Collection<IMagicEffect> effects
    ) {
        super(manaCost, damage, particleLifeTime, particleCount, effects);
        fillBlockActionMaps();
    }

    public FireCore() {
        this(DEFAULT_MANA_COST, DEFAULT_DAMAGE, DEFAULT_PARTICLE_LIFE_TIME, DEFAULT_STACK, DEFAULT_EFFECTS);
    }

    public FireCore(int stack) {
        this(DEFAULT_MANA_COST, DEFAULT_DAMAGE, DEFAULT_PARTICLE_LIFE_TIME, stack, DEFAULT_EFFECTS);
    }

    private void fillBlockActionMaps() {
        this.blockActionMap.put(TntBlock.class, FireCollision::collisionTnt);
    }

    @Override
    protected void commonProcessingBlock(Block block, Level level, BlockHitResult blockHitResult,
                                         ServerPlayer sender, Objects... objects) {

        var blockPos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(blockPos);

        if (state.isFlammable(level, blockPos, blockHitResult.getDirection())) {
            var abovePos = blockPos.relative(blockHitResult.getDirection());

            if (level.getBlockState(abovePos).isAir()) {
                level.setBlockAndUpdate(abovePos, Blocks.FIRE.defaultBlockState());
            }
        }
    }

    @Override
    public IMagicCore cloneWithArguments(Object... args) {
        return new FireCore((int) args[0]);
    }
}

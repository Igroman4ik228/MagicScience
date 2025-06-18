package com.magicscience.magicsciencemod.blocks;

import com.magicscience.magicsciencemod.MagicScienceMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MagicScienceMod.MOD_ID);

    public static final RegistryObject<Block> UPGRADER = BLOCKS.register("upgrader",
            () -> new UpgraderBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

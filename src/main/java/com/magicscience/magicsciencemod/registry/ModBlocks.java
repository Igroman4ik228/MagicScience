package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.blocks.MagicWorkbenchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, MagicScienceMod.MOD_ID);

    public static final RegistryObject<Block> MAGIC_WORKBENCH =
        BLOCKS.register("magic_workbench", () -> new MagicWorkbenchBlock(
            BlockBehaviour.Properties.of().strength(2.5f).noOcclusion()
        ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.blocks.entity.MagicWorkbenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MagicScienceMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<MagicWorkbenchBlockEntity>> MAGIC_WORKBENCH =
        BLOCK_ENTITIES.register("magic_workbench",
            () -> BlockEntityType.Builder.of(MagicWorkbenchBlockEntity::new,
                ModBlocks.MAGIC_WORKBENCH.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}


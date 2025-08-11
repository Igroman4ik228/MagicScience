package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.items.cast.MagicStick;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.magicscience.magicsciencemod.registry.ModBlocks.MAGIC_WORKBENCH;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MagicScienceMod.MOD_ID);

    public static final RegistryObject<Item> MAGIC_STICK = ITEMS.register("magic_stick",
        () -> new MagicStick(new Item.Properties()));

    public static final RegistryObject<Item> MAGIC_INK = ITEMS.register("magic_ink",
        () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> MAGIC_WORKBENCH_ITEM = ITEMS.register("magic_workbench",
        () -> new BlockItem(MAGIC_WORKBENCH.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

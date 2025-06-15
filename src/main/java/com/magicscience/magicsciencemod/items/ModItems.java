package com.magicscience.magicsciencemod.items;

import com.magicscience.magicsciencemod.MagicScienceMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Регистрация нового item
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MagicScienceMod.MOD_ID);

    // Регистрация конкретно EXAMPLE_SWORD и установка базовых модификаторов
    public static final RegistryObject<SwordItem> EXAMPLE_SWORD = ITEMS.register("example_sword",
            () -> new ExampleSwordItem(new CustomTier(), 7, -2.4F, new Item.Properties()));

    // Обязательная строка публичного метода
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

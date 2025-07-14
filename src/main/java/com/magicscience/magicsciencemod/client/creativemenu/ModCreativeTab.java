package com.magicscience.magicsciencemod.client.creativemenu;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.registry.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicScienceMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAGIC_SCIENCE_TAB = CREATIVE_MODE_TABS.register(
        "magic_science_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.magic_science_tab"))
            .icon(() -> new ItemStack(ModItems.MAGIC_STICK.get()))
            .displayItems(ModCreativeTab::registerDisplayItems)
            .build());

    private static void registerDisplayItems(
        CreativeModeTab.ItemDisplayParameters parameters,
        CreativeModeTab.Output output
    ) {
        output.accept(ModItems.MAGIC_STICK.get());
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

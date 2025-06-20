package com.magicscience.magicsciencemod.сreativeTab;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

//@Mod.EventBusSubscriber(modid = MagicScienceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicScienceMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAGIC_SCIENCE_TAB = CREATIVE_MODE_TABS.register("magic_science_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.magic_science_tab"))
                    .icon(() -> new ItemStack(ModItems.EXAMPLE_SWORD.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.EXAMPLE_SWORD.get());
                        output.accept(ModItems.EXAMPLE_STICK.get());
                        output.accept(ModItems.UPGRADER_BLOCK.get());
                        output.accept(ModItems.STICK_CRAFTER.get());
                        // Cюда другие предметы
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

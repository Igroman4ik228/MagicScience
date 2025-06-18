package com.magicscience.magicsciencemod.screen;

import com.magicscience.magicsciencemod.MagicScienceMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MagicScienceMod.MOD_ID);

    public static final RegistryObject<MenuType<UpgraderMenu>> UPGRADER_MENU =
            MENUS.register("upgrader_menu", () -> IForgeMenuType.create(UpgraderMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
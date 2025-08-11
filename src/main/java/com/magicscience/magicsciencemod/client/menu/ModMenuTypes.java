package com.magicscience.magicsciencemod.client.menu;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.blocks.entity.MagicWorkbenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, MagicScienceMod.MOD_ID);

    public static final RegistryObject<MenuType<MagicWorkbenchMenu>> MAGIC_WORKBENCH =
        MENUS.register("magic_workbench",
            () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity be = inv.player.level().getBlockEntity(pos);
                if (be instanceof MagicWorkbenchBlockEntity entity) {
                    return new MagicWorkbenchMenu(windowId, inv, entity);
                }
                return null;
            })
        );

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}

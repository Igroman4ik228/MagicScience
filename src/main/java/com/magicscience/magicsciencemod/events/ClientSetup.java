package com.magicscience.magicsciencemod.events;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.client.menu.ModMenuTypes;
import com.magicscience.magicsciencemod.client.screen.MagicWorkbenchScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MagicScienceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.MAGIC_WORKBENCH.get(), MagicWorkbenchScreen::new);
    }
}


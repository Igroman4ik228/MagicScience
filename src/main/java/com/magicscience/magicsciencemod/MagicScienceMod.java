package com.magicscience.magicsciencemod;

import com.magicscience.magicsciencemod.client.creativemenu.ModCreativeTab;
import com.magicscience.magicsciencemod.client.menu.ModMenuTypes;
import com.magicscience.magicsciencemod.events.ManaEvents;
import com.magicscience.magicsciencemod.events.ModCapabilityEvents;
import com.magicscience.magicsciencemod.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MagicScienceMod.MOD_ID)
public class MagicScienceMod {
    public static final String MOD_ID = "magicscience";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MagicScienceMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Network
        ModNetwork.register();

        // Blocks
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        // Menu
        ModMenuTypes.register(modEventBus);

        // Tabs
        ModCreativeTab.register(modEventBus);

        // Items
        ModItems.register(modEventBus);

        // Particles
        ModParticles.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        // Events
        MinecraftForge.EVENT_BUS.register(ModCapabilityEvents.class);
        MinecraftForge.EVENT_BUS.register(ManaEvents.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("SERVER START");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("SERVER STOP");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("CLIENT SETUP");
        }
    }
}

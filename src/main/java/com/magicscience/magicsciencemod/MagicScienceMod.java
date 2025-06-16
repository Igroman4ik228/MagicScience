package com.magicscience.magicsciencemod;

import com.magicscience.magicsciencemod.сreativeTab.ModCreativeTab;
import com.magicscience.magicsciencemod.blocks.LightBlockManager;
import com.magicscience.magicsciencemod.items.ModItems;
import com.magicscience.magicsciencemod.net.ModMessagesEnergy;
import com.magicscience.magicsciencemod.particle.ModParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
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
public class MagicScienceMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "magicscience";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MagicScienceMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Регистрация кастомных классов
        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModParticles.register(modEventBus);
        // .net пакеты
        ModMessagesEnergy.register();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("SERVER START");

        ServerLevel level = event.getServer().overworld();
        LightBlockManager.removeAllLights(level);
        LOGGER.info("Все световые блоки удалены при запуске сервера");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        ServerLevel level = event.getServer().overworld();
        LightBlockManager.removeAllLights(level);
        LOGGER.info("Все световые блоки удалены при остановке сервера");

        LOGGER.info("SERVER STOP");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("CLIENT SETUP");
        }
    }
}

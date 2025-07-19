package com.magicscience.magicsciencemod.events;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.mana.ManaProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MagicScienceMod.MOD_ID)
public class ModCapabilityEvents {

    public static final ResourceLocation MANA_ID = new ResourceLocation(MagicScienceMod.MOD_ID, "mana");

    @SubscribeEvent
    public static void attachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            var provider = new ManaProvider();
            event.addCapability(MANA_ID, provider);
            event.addListener(provider::invalidate);
        }
    }
}
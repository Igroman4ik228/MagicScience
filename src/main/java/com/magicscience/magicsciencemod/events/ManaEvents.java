package com.magicscience.magicsciencemod.events;

import com.magicscience.magicsciencemod.mana.ManaCapabilityHelper;
import com.magicscience.magicsciencemod.util.TimeHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ManaEvents {
    private static final String MANA_REGEN_COOLDOWN = "mana_regen_cd";
    private static final int COOLDOWN_TICKS = TimeHelper.seconds(2);

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer serverPlayer)) return;
        if (event.phase != TickEvent.Phase.END) return;

        var data = serverPlayer.getPersistentData();
        int cooldown = data.getInt(MANA_REGEN_COOLDOWN);

        if (cooldown > 0) {
            data.putInt(MANA_REGEN_COOLDOWN, cooldown - 1);
            return;
        }

        if (ManaCapabilityHelper.canAdd(serverPlayer, 1)) {
            ManaCapabilityHelper.addMana(serverPlayer, 10);
            data.putInt(MANA_REGEN_COOLDOWN, COOLDOWN_TICKS);
        }
    }
}

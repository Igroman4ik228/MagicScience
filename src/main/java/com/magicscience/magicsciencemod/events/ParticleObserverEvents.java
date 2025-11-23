package com.magicscience.magicsciencemod.events;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.network.magicparticles.ClientParticleObserverReassignPacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MagicScienceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ParticleObserverEvents {

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer exiting)) return;

        ServerLevel level = exiting.serverLevel();
        UUID exitingId = exiting.getUUID();

        ServerPlayer nearest = findNearestPlayer(level, exiting);
        UUID newId = nearest != null ? nearest.getUUID() : null;

        ModNetwork.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new ClientParticleObserverReassignPacket(exitingId, newId)
        );
    }

    private static ServerPlayer findNearestPlayer(ServerLevel level, ServerPlayer exiting) {
        double min = Double.MAX_VALUE;
        ServerPlayer result = null;

        for (ServerPlayer player : level.players()) {
            if (player == exiting) continue;

            double d = player.distanceToSqr(exiting);
            if (d < min) {
                min = d;
                result = player;
            }
        }

        return result;
    }
}

package com.magicscience.magicsciencemod.mana;

import com.magicscience.magicsciencemod.net.mana.ClientboundSyncManaPacket;
import com.magicscience.magicsciencemod.registry.ModCapabilities;
import com.magicscience.magicsciencemod.registry.ModMessagesMana;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;

import java.util.Optional;

public class ManaCapabilityHelper {

    public static Optional<IMana> get(Player player) {
        return player.getCapability(ModCapabilities.MANA_CAPABILITY).resolve();
    }

    public static void syncToClient(ServerPlayer player, IMana mana) {
        ModMessagesMana.CHANNEL.sendTo(
            new ClientboundSyncManaPacket(mana.getMana()),
            player.connection.connection,
            NetworkDirection.PLAY_TO_CLIENT
        );
    }

    public static void removeMana(Player player, int amount) {
        get(player).ifPresent(mana -> {
            mana.removeMana(amount);
            if (player instanceof ServerPlayer serverPlayer) {
                syncToClient(serverPlayer, mana);
            }
        });
    }

    public static void addMana(Player player, int amount) {
        get(player).ifPresent(mana -> {
            mana.addMana(amount);
            if (player instanceof ServerPlayer serverPlayer) {
                syncToClient(serverPlayer, mana);
            }
        });
    }

    public static void setMana(Player player, int amount) {
        get(player).ifPresent(mana -> {
            mana.setMana(amount);
            if (player instanceof ServerPlayer serverPlayer) {
                syncToClient(serverPlayer, mana);
            }
        });
    }

    public static boolean canAdd(Player player, int amount) {
        return get(player)
            .map(mana -> mana.getMana() + amount <= mana.getMaxMana())
            .orElse(false);
    }

    public static boolean canRemove(Player player, int amount) {
        return get(player).map(mana -> mana.getMana() >= amount).orElse(false);
    }

}

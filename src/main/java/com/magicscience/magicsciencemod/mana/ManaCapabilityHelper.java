package com.magicscience.magicsciencemod.mana;

import com.magicscience.magicsciencemod.network.mana.ClientSyncManaPacket;
import com.magicscience.magicsciencemod.registry.ModCapabilities;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class ManaCapabilityHelper {
    public static Optional<IMana> get(@NotNull Player player) {
        return player.getCapability(ModCapabilities.MANA_CAPABILITY).resolve();
    }

    public static void syncToClient(@NotNull ServerPlayer player, @NotNull IMana mana) {
        ModNetwork.CHANNEL.sendTo(
            new ClientSyncManaPacket(mana.getMana()),
            player.connection.connection,
            NetworkDirection.PLAY_TO_CLIENT
        );
    }

    private static void modifyAndSync(@NotNull Player player, @NotNull Consumer<IMana> action) {
        get(player).ifPresent(mana -> {
            action.accept(mana);
            if (player instanceof ServerPlayer serverPlayer) {
                syncToClient(serverPlayer, mana);
            }
        });
    }

    public static void removeMana(@NotNull Player player, int amount) {

        modifyAndSync(player, mana -> mana.removeMana(amount));
    }

    public static void addMana(@NotNull Player player, int amount) {
        modifyAndSync(player, mana -> mana.addMana(amount));
    }

    public static void setMana(@NotNull Player player, int amount) {
        modifyAndSync(player, mana -> mana.setMana(amount));
    }

    public static boolean canAdd(@NotNull Player player, int amount) {
        return get(player)
            .map(mana -> mana.getMana() + amount <= mana.getMaxMana())
            .orElse(false);
    }

    public static boolean canRemove(@NotNull Player player, int amount) {
        return get(player)
            .map(mana -> mana.getMana() >= amount)
            .orElse(false);
    }
}

package com.magicscience.magicsciencemod.network;

import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public interface IClientPacket extends IPacket {
    void handle(@NotNull LocalPlayer player);
}

package com.magicscience.magicsciencemod.network;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface IServerPacket extends IPacket {
    void handle(@NotNull ServerPlayer player);
}

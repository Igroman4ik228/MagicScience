package com.magicscience.magicsciencemod.network;

import net.minecraft.server.level.ServerPlayer;

public interface IServerPacket extends IPacket {
    void handle(ServerPlayer player);
}

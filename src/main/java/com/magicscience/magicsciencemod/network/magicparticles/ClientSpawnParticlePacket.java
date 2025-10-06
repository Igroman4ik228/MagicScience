package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.client.particles.aspecthandlers.MagicParticleCreator;
import com.magicscience.magicsciencemod.items.cast.CastData;
import com.magicscience.magicsciencemod.network.IClientPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ClientSpawnParticlePacket implements IClientPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull CastData castData;

    public ClientSpawnParticlePacket(@NotNull CastData castData) {
        this.castData = castData;
    }

    public ClientSpawnParticlePacket(FriendlyByteBuf buf) {
        this.castData = CastData.decode(buf);
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        castData.encode(buf);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(@NotNull LocalPlayer player) {
        LOGGER.info("Received ClientboundSpawnParticlePacket:");
        LOGGER.info("castData: {}", castData);

        var pos = player.position();
        LOGGER.info("  Position: x = {}, y = {}, z = {}", pos.x, pos.y, pos.z);

        new MagicParticleCreator(castData).create();
    }
}

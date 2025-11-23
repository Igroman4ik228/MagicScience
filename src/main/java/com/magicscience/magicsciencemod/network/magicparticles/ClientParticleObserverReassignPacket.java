package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.client.particles.ParticleRegistry;
import com.magicscience.magicsciencemod.network.IClientPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class ClientParticleObserverReassignPacket implements IClientPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull UUID oldObserver;
    private final UUID newObserver;

    public ClientParticleObserverReassignPacket(@NotNull UUID oldObserver, UUID newObserver) {
        this.oldObserver = oldObserver;
        this.newObserver = newObserver;
    }

    public ClientParticleObserverReassignPacket(FriendlyByteBuf buf) {
        this.oldObserver = buf.readUUID();
        this.newObserver = buf.readBoolean() ? buf.readUUID() : null;
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeUUID(oldObserver);
        buf.writeBoolean(newObserver != null);
        if (newObserver != null) buf.writeUUID(newObserver);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(@NotNull LocalPlayer player) {
        LOGGER.info("Received ClientParticleObserverReassignPacket:");
        LOGGER.info("oldObserver: {}, newObserver: {}", oldObserver, newObserver);

        for (MagicParticle p : ParticleRegistry.getActiveParticles()) {
            if (oldObserver.equals(p.getObserverClientUUID())) {
                p.setObserverClientUUID(newObserver);
            }
        }
    }
}

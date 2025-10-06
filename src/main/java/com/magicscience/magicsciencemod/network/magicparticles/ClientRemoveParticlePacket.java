package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.client.particles.ParticleRegistry;
import com.magicscience.magicsciencemod.network.IClientPacket;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClientRemoveParticlePacket implements IClientPacket {
    private final @NotNull UUID particleUUID;

    public ClientRemoveParticlePacket(@NotNull UUID particleUUID) {
        this.particleUUID = particleUUID;
    }

    public ClientRemoveParticlePacket(FriendlyByteBuf buf) {
        this.particleUUID = buf.readUUID();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeUUID(particleUUID);
    }

    @Override
    public void handle(@NotNull LocalPlayer player) {
        MagicParticle particle = ParticleRegistry.get(particleUUID);
        if (particle==null) return;
        particle.remove();
    }
}

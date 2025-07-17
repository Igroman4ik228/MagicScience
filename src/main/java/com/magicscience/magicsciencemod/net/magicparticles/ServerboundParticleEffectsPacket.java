package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ServerboundParticleEffectsPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int entityId;
    private final int coreId;

    public ServerboundParticleEffectsPacket(int entityId, int coreId) {
        this.entityId = entityId;
        this.coreId = coreId;
    }

    public ServerboundParticleEffectsPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.coreId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(coreId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            LOGGER.info("ServerboundParticleEffectsPacket start");

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            // get entity with collision
            Level level = player.level();
            Entity target = level.getEntity(entityId);
            if (target == null) return;

            // get list of effects for current core
            var effects = CoreTypeHelper.findInstance(coreId).getMagicEffects();

            // apply effects
            for (IMagicEffect effect : effects) {
                effect.applyEffect(target);
            }

        });
        ctx.get().setPacketHandled(true);
    }
}

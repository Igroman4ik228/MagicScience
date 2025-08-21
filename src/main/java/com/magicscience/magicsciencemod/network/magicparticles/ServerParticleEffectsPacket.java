package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class ServerParticleEffectsPacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int entityId;
    private final int coreId;

    public ServerParticleEffectsPacket(int entityId, int coreId) {
        this.entityId = entityId;
        this.coreId = coreId;
    }

    public ServerParticleEffectsPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.coreId = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(coreId);
    }

    @Override
    public void handle(ServerPlayer player) {
        LOGGER.info("ServerboundParticleEffectsPacket start");

        // get entity with collision
        var level = player.serverLevel();
        Entity target = level.getEntity(entityId);
        if (target==null) return;

        // get list of effects for current core
        var effects = CoreTypeHelper.findInstance(coreId).getMagicEffects();

        // apply effects
        for (IMagicEffect effect : effects) {
            effect.applyEffect(target);
        }
    }
}

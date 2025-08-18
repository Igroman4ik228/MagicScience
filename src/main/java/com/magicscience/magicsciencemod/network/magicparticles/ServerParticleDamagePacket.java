package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.network.IServerPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class ServerParticleDamagePacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int entityId;
    private final float damage;
    private final int ownerId;

    public ServerParticleDamagePacket(int entityId, float damage, int ownerId) {
        this.entityId = entityId;
        this.damage = damage;
        this.ownerId = ownerId;
    }

    public ServerParticleDamagePacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.damage = buf.readFloat();
        this.ownerId = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(damage);
        buf.writeInt(ownerId);
    }

    @Override
    public void handle(ServerPlayer player) {
        LOGGER.info("ServerboundParticleDamagePacket start");

        // get entity with collision
        var level = player.serverLevel();
        Entity target = level.getEntity(entityId);
        if (target==null) return;

        // Get particle owner
        Entity owner = level.getEntity(ownerId);
        if (!(owner instanceof ServerPlayer ownerPlayer)) return;

        LOGGER.info("ownerId: {}", ownerId);

        // Damage
        target.hurt(target.damageSources().playerAttack(ownerPlayer), damage);
    }
}

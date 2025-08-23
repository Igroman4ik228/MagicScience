package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class ServerParticleEntityHitPacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int entityId;
    private final int coreId;
    private final float damage;
    private final int ownerId;

    public ServerParticleEntityHitPacket(int entityId, int coreId, float damage, int ownerId) {
        this.entityId = entityId;
        this.coreId = coreId;
        this.damage = damage;
        this.ownerId = ownerId;
    }

    public ServerParticleEntityHitPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.coreId = buf.readInt();
        this.damage = buf.readFloat();
        this.ownerId = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(coreId);
        buf.writeFloat(damage);
        buf.writeInt(ownerId);
    }

    @Override
    public void handle(ServerPlayer player) {
        // get entity with collision
        var level = player.serverLevel();
        Entity target = level.getEntity(entityId);
        if (target==null) return;

        // Effect
        // get list of effects for current core
        var effects = CoreTypeHelper.findInstance(coreId).getMagicEffects();

        // apply effects
        for (IMagicEffect effect : effects) {
            effect.applyEffect(target);
        }

        // Damage
        // Get particle owner
        Entity owner = level.getEntity(ownerId);
        if (!(owner instanceof ServerPlayer ownerPlayer)) return;

        // Damage
        target.hurt(target.damageSources().playerAttack(ownerPlayer), damage);
    }
}

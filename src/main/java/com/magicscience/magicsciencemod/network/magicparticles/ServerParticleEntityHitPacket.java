package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class ServerParticleEntityHitPacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int entityId;
    private final int coreId;
    private final float damage;
    private final @NotNull UUID ownerUUID;

    public ServerParticleEntityHitPacket(int entityId, int coreId, float damage, @NotNull UUID ownerUUID) {
        this.entityId = entityId;
        this.coreId = coreId;
        this.damage = damage;
        this.ownerUUID = ownerUUID;
    }

    public ServerParticleEntityHitPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.coreId = buf.readInt();
        this.damage = buf.readFloat();
        this.ownerUUID = buf.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(coreId);
        buf.writeFloat(damage);
        buf.writeUUID(ownerUUID);
    }

    @Override
    public void handle(ServerPlayer player) {
        // get entity with collision
        var level = player.serverLevel();
        Entity target = level.getEntity(entityId);
        if (target==null) return;


        // Core collision
        var core = CoreTypeHelper.findInstance(coreId);
        core.processingEntity(target, player);

        // Effect
        // get list of effects for current core
        var effects = CoreTypeHelper.findInstance(coreId).getMagicEffects();

        // apply effects
        for (IMagicEffect effect : effects) {
            effect.applyEffect(target);
        }

        // Damage
        // Get particle owner
        var owner = level.getPlayerByUUID(ownerUUID);
        if (!(owner instanceof ServerPlayer ownerPlayer)) return;

        // Damage
        target.hurt(target.damageSources().playerAttack(ownerPlayer), damage);
    }
}

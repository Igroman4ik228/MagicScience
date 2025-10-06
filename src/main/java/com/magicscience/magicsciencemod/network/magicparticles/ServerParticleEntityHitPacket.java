package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.cores.effects.IMagicEffect;
import com.magicscience.magicsciencemod.network.IServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ServerParticleEntityHitPacket implements IServerPacket {
    private final @NotNull UUID entityUUID;
    private final int coreId;
    private final float damage;
    private final @NotNull UUID ownerUUID;

    public ServerParticleEntityHitPacket(@NotNull UUID entityUUID, int coreId, float damage, @NotNull UUID ownerUUID) {
        this.entityUUID = entityUUID;
        this.coreId = coreId;
        this.damage = damage;
        this.ownerUUID = ownerUUID;
    }

    public ServerParticleEntityHitPacket(FriendlyByteBuf buf) {
        this.entityUUID = buf.readUUID();
        this.coreId = buf.readInt();
        this.damage = buf.readFloat();
        this.ownerUUID = buf.readUUID();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeUUID(entityUUID);
        buf.writeInt(coreId);
        buf.writeFloat(damage);
        buf.writeUUID(ownerUUID);
    }

    @Override
    public void handle(@NotNull ServerPlayer player) {
        // get entity with collision
        var level = player.serverLevel();
        Entity target = level.getEntity(entityUUID);
        if (target==null)
            return;

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
        if (owner==null)
            return;

        // Damage
        target.hurt(target.damageSources().playerAttack(owner), damage);
    }
}

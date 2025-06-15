package com.magicscience.magicsciencemod.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundParticleCollisionPacket {
    private final int entityId;
    private final float damage;

    public ServerboundParticleCollisionPacket(int entityId, float damage) {
        this.entityId = entityId;
        this.damage = damage;
    }

    public ServerboundParticleCollisionPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.damage = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(damage);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        // Обработка коллиции(соприкосновение с другим энтити) на сервере
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                // Получение энтити, который  соприкоснулся
                Entity target = player.level().getEntity(entityId);
                if (target != null) {
                    // Дамаг и поджог
                    target.hurt(target.damageSources().playerAttack(player), damage);
                    target.setSecondsOnFire(5);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


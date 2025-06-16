package com.magicscience.magicsciencemod.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundParticleCollisionPacket {
    private final int entityId;
    private final float damage;
    private final int ownerId;

    public ServerboundParticleCollisionPacket(int entityId, float damage, int ownerId) {
        this.entityId = entityId;
        this.damage = damage;
        this.ownerId = ownerId;
    }

    public ServerboundParticleCollisionPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.damage = buf.readFloat();
        this.ownerId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(damage);
        buf.writeInt(ownerId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Level level = sender.level();

            // Получаем сущность-цель
            Entity target = level.getEntity(entityId);
            if (target == null) return;

            // Получаем владельца частицы
            Entity owner = level.getEntity(ownerId);
            if (!(owner instanceof ServerPlayer)) {
                // Если владелец не игрок (или не найден), не наносим урон
                return;
            }

            ServerPlayer ownerPlayer = (ServerPlayer) owner;

            // Наносим урон от имени владельца частицы
            target.hurt(target.damageSources().playerAttack(ownerPlayer), damage);
            target.setSecondsOnFire(5);

        });
        ctx.get().setPacketHandled(true);
    }

//    public void handle(Supplier<NetworkEvent.Context> ctx) {
//        // Обработка коллиции(соприкосновение с другим энтити) на сервере
//        ctx.get().enqueueWork(() -> {
//            ServerPlayer player = ctx.get().getSender();
//            if (player != null) {
//                // Получение энтити, который  соприкоснулся
//                Entity target = player.level().getEntity(entityId);
//                if (target != null) {
//                    // Дамаг и поджог
//                    target.hurt(target.damageSources().playerAttack(), damage);
//                    target.setSecondsOnFire(5);
//                }
//            }
//        });
//        ctx.get().setPacketHandled(true);
//    }
}


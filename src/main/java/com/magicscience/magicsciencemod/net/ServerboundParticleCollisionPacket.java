package com.magicscience.magicsciencemod.net;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ServerboundParticleCollisionPacket {
    private static final Logger LOGGER = LogUtils.getLogger();
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
        // Обработка коллиции(соприкосновение с другим энтити) на сервере
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            // Получение энтити, который  соприкоснулся
            Level level = player.level();
            Entity target = level.getEntity(entityId);
            if (target == null) return;

            // Получаем владельца частицы
            Entity owner = level.getEntity(ownerId);
            if (!(owner instanceof ServerPlayer ownerPlayer)) {
                // Если владелец не игрок (или не найден), не наносим урон
                return;
            }

            // Дамаг и поджог
            target.hurt(target.damageSources().playerAttack(ownerPlayer), damage);
            target.setSecondsOnFire(5);

            if (target instanceof Creeper creeper) {
                creeper.ignite();
            }

            LOGGER.info("level = {}", level);
            LOGGER.info("target = {}", target);
            LOGGER.info("player = {}", player);
        });
        ctx.get().setPacketHandled(true);
    }
}


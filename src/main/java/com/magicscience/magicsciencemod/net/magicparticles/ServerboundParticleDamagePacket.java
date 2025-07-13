package com.magicscience.magicsciencemod.net.magicparticles;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ServerboundParticleDamagePacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int entityId;
    private final float damage;
    private final int ownerId;

    public ServerboundParticleDamagePacket(int entityId, float damage, int ownerId) {
        this.entityId = entityId;
        this.damage = damage;
        this.ownerId = ownerId;
    }

    public ServerboundParticleDamagePacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.damage = buf.readFloat();
        this.ownerId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(damage);
        buf.writeInt(ownerId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            LOGGER.info("ServerboundParticleDamagePacket start");

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            // get entity with collision
            Level level = player.level();
            Entity target = level.getEntity(entityId);
            if (target == null) return;

            // Получаем владельца частицы
            Entity owner = level.getEntity(ownerId);

            // Если владелец не игрок (или не найден), не наносим урон
            if (!(owner instanceof ServerPlayer ownerPlayer)) return;

            LOGGER.info("ownerId: " + ownerId);

            // Дамаг
            target.hurt(target.damageSources().playerAttack(ownerPlayer), damage);

        });
        ctx.get().setPacketHandled(true);
    }
}

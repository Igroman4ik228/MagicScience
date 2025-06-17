package com.magicscience.magicsciencemod.net;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ServerboundCastParticlePacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final int ownerId;
    private final boolean isStick;

    private final Vec3 position;
    private final Vec3 direction;
    private final int particleCount;
    private final float damage;
    private final float radius;

    public ServerboundCastParticlePacket(int ownerId, Vec3 position, Vec3 direction, int particleCount, float damage, float radius, boolean isStick) {
        this.ownerId = ownerId;
        this.position = position;
        this.direction = direction;
        this.particleCount = particleCount;
        this.damage = damage;
        this.radius = radius;
        this.isStick = isStick;
    }

    public ServerboundCastParticlePacket(FriendlyByteBuf buf) {
        this.ownerId = buf.readInt();
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.particleCount = buf.readInt();
        this.damage = buf.readFloat();
        this.radius = buf.readFloat();
        this.isStick = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ownerId);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
        buf.writeInt(particleCount);
        buf.writeFloat(damage);
        buf.writeFloat(radius);
        buf.writeBoolean(isStick);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        // Логика сервера
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                // Отправка пакета клинту
                ModMessagesEnergy.CHANNEL.send(
                        // Радиус отправки пакета клинтам, может нескольким
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                        // Отправки пакета клинтам пакетов с партиками
                        new ClientboundSpawnParticlePacket(ownerId, position, direction, particleCount, damage, radius, isStick)
                );
                LOGGER.info("Send to pl");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

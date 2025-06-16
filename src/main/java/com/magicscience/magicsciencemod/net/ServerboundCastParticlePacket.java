package com.magicscience.magicsciencemod.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ServerboundCastParticlePacket {
    private final Vec3 position;
    private final Vec3 direction;
    private final int particleCount;
    private final float damage;
    private final float radius;

    public ServerboundCastParticlePacket(Vec3 position, Vec3 direction, int particleCount, float damage, float radius) {
        this.position = position;
        this.direction = direction;
        this.particleCount = particleCount;
        this.damage = damage;
        this.radius = radius;
    }

    public ServerboundCastParticlePacket(FriendlyByteBuf buf) {
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.particleCount = buf.readInt();
        this.damage = buf.readFloat();
        this.radius = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
        buf.writeInt(particleCount);
        buf.writeFloat(damage);
        buf.writeFloat(radius);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        // Логика сервера
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                // Отправка пакета клинту
                ModMessagesEnergy.CHANNEL.send(
                        // Радиус отправки пакета клиентам, может нескольким
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                        // Отправки пакета клиентам пакетов с партиками
                        new ClientboundSpawnParticlePacket(position, direction, particleCount, damage, radius)
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

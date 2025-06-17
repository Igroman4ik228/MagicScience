package com.magicscience.magicsciencemod.net;

import com.magicscience.magicsciencemod.particle.EnergyParticleOptions;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ClientboundSpawnParticlePacket {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final int ownerId;

    private final Vec3 position;
    private final Vec3 direction;
    private final int particleCount;
    private final float damage;
    private final float radius;
    private final boolean isStick;

    public ClientboundSpawnParticlePacket(int ownerId, Vec3 position, Vec3 direction, int particleCount, float damage, float radius, boolean isStick) {
        this.ownerId = ownerId;
        this.position = position;
        this.direction = direction;
        this.particleCount = particleCount;
        this.damage = damage;
        this.radius = radius;
        this.isStick = isStick;
    }

    public ClientboundSpawnParticlePacket(FriendlyByteBuf buf) {
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
        // Логика клиента
        ctx.get().enqueueWork(() -> {
            // Мир (level)
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                if (isStick){
                    spawnParticlesStick(level, position, particleCount, radius);
                }
                else {
                    spawnParticles(level, position, direction, particleCount, radius);
                }

                LOGGER.info("Spawn handle end");
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void spawnParticles(Level level, Vec3 center, Vec3 direction, int count, float radius) {
        for (int i = 0; i < count; i++) {
            double theta = Math.random() * 2 * Math.PI;
            double phi = Math.random() * Math.PI;

            double x = center.x + radius * Math.sin(phi) * Math.cos(theta);
            double y = center.y + radius * Math.cos(phi);
            double z = center.z + radius * Math.sin(phi) * Math.sin(theta);

            level.addParticle(new EnergyParticleOptions(100, ownerId, false), x, y, z, direction.x, direction.y, direction.z);
        }


        LOGGER.info("Spawn!");
    }

    private void spawnParticlesStick(Level level, Vec3 center, int count, float radius) {
        for (int i = 0; i < count; i++) {
            // Случайный угол в плоскости XZ (горизонтально)
            double angle = Math.random() * 2 * Math.PI;

            // Небольшой случайный разброс по радиусу (80%-100% от основного радиуса)
            double r = radius * (0.8 + 0.2 * Math.random());

            // Вычисляем позицию в плоскости
            double x = center.x + r * Math.cos(angle);
            double z = center.z + r * Math.sin(angle);

            // Небольшой разброс по высоте (если нужно вертикально, поменяйте y и z/x)
            double y = center.y + (Math.random() - 0.5) * 0.2; // +-0.1 блока по высоте

            // Направление - от центра наружу (можно добавить случайное отклонение)
            Vec3 particleDir = new Vec3(x - center.x, 0, z - center.z)
                    .normalize()
                    .scale(0.1 + Math.random() * 0.2); // Скорость частиц

            level.addParticle(
                    new EnergyParticleOptions(100, ownerId, true),
                    x, y, z,
                    particleDir.x, particleDir.y, particleDir.z
            );
        }
    }
}


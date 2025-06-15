package com.magicscience.magicsciencemod.net;

import com.magicscience.magicsciencemod.particle.ModParticles;
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

    private final Vec3 position;
    private final Vec3 direction;
    private final int particleCount;
    private final float damage;
    private final float radius;

    public ClientboundSpawnParticlePacket(Vec3 position, Vec3 direction, int particleCount, float damage, float radius) {
        this.position = position;
        this.direction = direction;
        this.particleCount = particleCount;
        this.damage = damage;
        this.radius = radius;
    }

    public ClientboundSpawnParticlePacket(FriendlyByteBuf buf) {
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
        // Логика клиента
        ctx.get().enqueueWork(() -> {
            // Мир (level)
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                spawnParticles(level, position, direction, particleCount, radius);
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

            level.addParticle(ModParticles.ENERGY_PARTICLE.get(), x, y, z, direction.x, direction.y, direction.z);
        }

        LOGGER.info("Spawn!");
    }
}


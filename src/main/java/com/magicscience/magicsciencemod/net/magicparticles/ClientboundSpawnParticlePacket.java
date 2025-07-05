package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.SpellData;
import com.magicscience.magicsciencemod.particles.MagicParticleOptions;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ClientboundSpawnParticlePacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final SpellData spellData;
    private final Vec3 position;
    private final Vec3 direction;

    public ClientboundSpawnParticlePacket(SpellData spellData, Vec3 position, Vec3 direction) {
        this.spellData = spellData;
        this.position = position;
        this.direction = direction;
    }

    public ClientboundSpawnParticlePacket(FriendlyByteBuf buf) {
        this.spellData = new SpellData(
            buf.readInt(),               // ownerId
            buf.readVarInt(),            // coreId
            buf.readVarIntArray(),       // attributeIds
            buf.readVarInt(),            // structureId
            buf.readInt(),                // particleSpeed
            buf.readInt()                  // particleLifeTime
        );
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(spellData.ownerId());
        buf.writeVarInt(spellData.coreId());
        buf.writeVarInt(spellData.attributeIds().length);
        for (int attrId : spellData.attributeIds()) {
            buf.writeVarInt(attrId);
        }
        buf.writeVarInt(spellData.structureId());
        buf.writeInt(spellData.particleSpeed());
        buf.writeInt(spellData.particleLifeTime());

        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
    }

    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            LOGGER.info("Received ClientboundSpawnParticlePacket:");
            LOGGER.info("  Owner ID: {}", spellData.ownerId());
            LOGGER.info("  Core ID: {}", spellData.coreId());
            LOGGER.info("  Attribute IDs: {}", java.util.Arrays.toString(spellData.attributeIds()));
            LOGGER.info("  Structure ID: {}", spellData.structureId());
            LOGGER.info("  Particle Speed: {}", spellData.particleSpeed());

            LOGGER.info("  Position: x = {}, y = {}, z = {}", position.x, position.y, position.z);
            LOGGER.info("  Direction: x = {}, y = {}, z = {}", direction.x, direction.y, direction.z);

            spawnParticles();
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles() {
        var level = Minecraft.getInstance().level;
        if (level == null) return;

        int particleCount = 10;

        boolean isWithStructure = spellData.structureId() != 0;
        boolean isWithAttribute = spellData.attributeIds().length != 0;

        for (int i = 0; i < particleCount; i++) {
            double velocityX, velocityY, velocityZ;

            // ToDo: Вычисления перенести в Math
            double spread = 0.3; // размер разброса позиции
            velocityX = 0;
            velocityY = 0;
            velocityZ = 0;

            // Смещение позиции будет учитываться при добавлении частицы ниже
            double offsetX = (Math.random() - 0.5) * spread;
            double offsetY = (Math.random() - 0.5) * spread;
            double offsetZ = (Math.random() - 0.5) * spread;

            level.addParticle(
                new MagicParticleOptions(
                    spellData.ownerId(),
                    spellData.coreId(),
                    spellData.attributeIds(),
                    spellData.structureId(),
                    spellData.particleSpeed(),
                    spellData.particleLifeTime()
                ),
                position.x + offsetX,
                position.y + offsetY,
                position.z + offsetZ,
                velocityX, velocityY, velocityZ
            );

        }
    }
}

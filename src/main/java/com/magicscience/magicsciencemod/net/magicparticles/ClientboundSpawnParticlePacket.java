package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.MagicParticleCreator;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ClientboundSpawnParticlePacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull SpellData spellData;
    private final @NotNull Vec3 position;
    private final @NotNull Vec3 direction;

    public ClientboundSpawnParticlePacket(@NotNull SpellData spellData, @NotNull Vec3 position, @NotNull Vec3 direction) {
        this.spellData = spellData;
        this.position = position;
        this.direction = direction;
    }

    public ClientboundSpawnParticlePacket(FriendlyByteBuf buf) {
        this.spellData = new SpellData(
            buf.readInt(),               // ownerId
            buf.readVarInt(),            // coreId
            buf.readVarInt(),            // coreStack
            buf.readVarIntArray(),       // attributeIds
            buf.readVarIntArray(),       // attributeStack
            buf.readVarInt(),            // structureId
            buf.readVarInt(),            // structureStack
            buf.readInt(),                // particleSpeed
            buf.readInt()                  // particleLifeTime
        );
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(spellData.ownerId());
        buf.writeVarInt(spellData.coreId());
        buf.writeVarInt(spellData.coreStack());

        buf.writeVarIntArray(spellData.attributeIds());
        buf.writeVarIntArray(spellData.attributeStack());

        buf.writeVarInt(spellData.structureId());
        buf.writeVarInt(spellData.structureStack());

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
            if (player==null) return;

            LOGGER.info("Received ClientboundSpawnParticlePacket:");
            LOGGER.info("  Owner ID: {}", spellData.ownerId());
            LOGGER.info("  Core ID: {}", spellData.coreId());
            LOGGER.info("  Core Stack: {}", spellData.coreStack());

            LOGGER.info("  Attribute IDs: {}", java.util.Arrays.toString(spellData.attributeIds()));
            LOGGER.info("  Attribute Stack: {}", java.util.Arrays.toString(spellData.attributeStack()));

            LOGGER.info("  Structure ID: {}", spellData.structureId());
            LOGGER.info("  Structure ID: {}", spellData.structureStack());

            LOGGER.info("  Particle Speed: {}", spellData.particleSpeed());

            LOGGER.info("  Position: x = {}, y = {}, z = {}", position.x, position.y, position.z);
            LOGGER.info("  Direction: x = {}, y = {}, z = {}", direction.x, direction.y, direction.z);

            var pos = player.position();
            LOGGER.info("  Position: x = {}, y = {}, z = {}", pos.x, pos.y, pos.z);

            var particleCreator = new MagicParticleCreator(spellData, position, direction);
            particleCreator.create();
        });
        ctx.get().setPacketHandled(true);
    }
}

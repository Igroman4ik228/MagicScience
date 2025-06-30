package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.Spell;
import com.magicscience.magicsciencemod.aspects.SpellData;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import com.mojang.logging.LogUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ServerboundCastParticlePacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final SpellData spellData;

    public ServerboundCastParticlePacket(Spell spell) {
        this.spellData = spell.toData();
    }

    public ServerboundCastParticlePacket(FriendlyByteBuf buf) {
        int ownerId = buf.readInt();
        int coreId = buf.readVarInt();
        int attrCount = buf.readVarInt();
        int[] attrs = new int[attrCount];
        for (int i = 0; i < attrCount; i++) attrs[i] = buf.readVarInt();
        int structureId = buf.readVarInt();
        int speed = buf.readInt();

        this.spellData = new SpellData(ownerId, coreId, attrs, structureId, speed);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(spellData.ownerId());
        buf.writeVarInt(spellData.coreId());
        buf.writeVarInt(spellData.attributeIds().length);
        for (int id : spellData.attributeIds()) buf.writeVarInt(id);
        buf.writeVarInt(spellData.structureId());
        buf.writeInt(spellData.particleSpeed());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            LOGGER.info("SpellData received:");
            LOGGER.info("  Owner ID: {}", spellData.ownerId());
            LOGGER.info("  Core ID: {}", spellData.coreId());
            LOGGER.info("  Attribute IDs: {}", java.util.Arrays.toString(spellData.attributeIds()));
            LOGGER.info("  Structure ID: {}", spellData.structureId());
            LOGGER.info("  Particle Speed: {}", spellData.particleSpeed());

            LOGGER.info("Packet handled and data logged for player {}", player.getName().getString());

            ModMessagesMagicParticles.CHANNEL.send(
                    // Радиус отправки пакета клинтам, может нескольким
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                    // Отправки пакета клинтам пакетов с партиками
                    new ClientboundSpawnParticlePacket(
                            spellData,
                            player.position().add(0, 1, 0),
                            player.getLookAngle().normalize()
                    )
            );
        });
        ctx.get().setPacketHandled(true);
    }
}

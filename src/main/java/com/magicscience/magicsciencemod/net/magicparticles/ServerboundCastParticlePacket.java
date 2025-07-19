package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.spell.Spell;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.mana.ManaCapabilityHelper;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.function.Supplier;

public class ServerboundCastParticlePacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull SpellData spellData;

    public ServerboundCastParticlePacket(Spell spell) {
        this.spellData = SpellConverter.toData(spell);
    }

    public ServerboundCastParticlePacket(FriendlyByteBuf buf) {
        int ownerId = buf.readInt();
        int coreId = buf.readVarInt();
        int coreStack = buf.readVarInt();
        int attrCount = buf.readVarInt();
        int[] attrs = new int[attrCount];
        for (int i = 0; i < attrCount; i++) attrs[i] = buf.readVarInt();
        int[] attrsStack = new int[attrCount];
        for (int i = 0; i < attrCount; i++) attrsStack[i] = buf.readVarInt();
        int structureId = buf.readVarInt();
        int structureStack = buf.readVarInt();
        int particleSpeed = buf.readInt();
        int particleLifeTime = buf.readInt();

        this.spellData = new SpellData(
            ownerId,
            coreId,
            coreStack,
            attrs,
            attrsStack,
            structureId,
            structureStack,
            particleSpeed,
            particleLifeTime
        );
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(spellData.ownerId());
        buf.writeVarInt(spellData.coreId());
        buf.writeVarInt(spellData.coreStack());
        buf.writeVarInt(spellData.attributeIds().length);
        for (int id : spellData.attributeIds()) buf.writeVarInt(id);
        for (int id : spellData.attributeStack()) buf.writeVarInt(id);
        buf.writeVarInt(spellData.structureId());
        buf.writeVarInt(spellData.structureStack());
        buf.writeInt(spellData.particleSpeed());
        buf.writeInt(spellData.particleLifeTime());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Spell spell = SpellConverter.toSpell(spellData);

            var mana = ManaCapabilityHelper.get(player).get().getMana();

            if (mana < spell.getManaCost()) return;

            LOGGER.info("SpellData received:");
            LOGGER.info("  Mana: {}", mana);

            LOGGER.info("  Owner ID: {}", spellData.ownerId());

            LOGGER.info("  Core ID: {}", spellData.coreId());
            LOGGER.info("  Core Stack: {}", spellData.coreStack());

            LOGGER.info("  Attribute IDs: {}", Arrays.toString(spellData.attributeIds()));
            LOGGER.info("  Attribute Stack: {}", Arrays.toString(spellData.attributeStack()));

            LOGGER.info("  Structure ID: {}", spellData.structureId());
            LOGGER.info("  Structure Stack: {}", spellData.structureStack());

            LOGGER.info("Packet handled and data logged for player {}", player.getName().getString());

            ModMessagesMagicParticles.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),

                new ClientboundSpawnParticlePacket(
                    spellData,
                    player.position().add(0, 1.4, 0),
                    player.getLookAngle().normalize().scale(spell.getParticleSpeed())
                )
            );

            ManaCapabilityHelper.removeMana(player, spell.getManaCost());

            LOGGER.info("Spawn");

        });
        ctx.get().setPacketHandled(true);
    }
}

package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.spell.Spell;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.mana.ManaCapabilityHelper;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ServerCastParticlePacket implements IServerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull SpellData spellData;

    public ServerCastParticlePacket(@NotNull SpellData spellData) {
        this.spellData = spellData;
    }

    public ServerCastParticlePacket(FriendlyByteBuf buf) {
        int ownerId = buf.readInt();
        int coreId = buf.readVarInt();
        int coreStack = buf.readVarInt();
        int[] attributeIds = buf.readVarIntArray();
        int[] attributeStacks = buf.readVarIntArray();
        int structureId = buf.readVarInt();
        int structureStack = buf.readVarInt();
        int particleSpeed = buf.readInt();
        int particleLifeTime = buf.readInt();

        this.spellData = new SpellData(
            ownerId,
            coreId,
            coreStack,
            attributeIds,
            attributeStacks,
            structureId,
            structureStack,
            particleSpeed,
            particleLifeTime
        );
    }

    @Override
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
    }

    @Override
    public void handle(ServerPlayer player) {
        if (player.getId()!=spellData.ownerId())
            return;

        Spell spell = SpellConverter.toSpell(spellData);

        int mana = ManaCapabilityHelper.get(player).get().getMana();
        LOGGER.info("ServerboundCastParticlePacket");
        LOGGER.info("SpellData received:");
        LOGGER.info("  Mana: {}", mana);
        LOGGER.info("  SpellData: {}", spellData);

        if (!player.isCreative()) {
            if (ManaCapabilityHelper.canRemove(player, spell.getManaCost()))
                return;

            ManaCapabilityHelper.removeMana(player, spell.getManaCost());
        }

        ModNetwork.CHANNEL.send(
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
            new ClientSpawnParticlePacket(
                spellData,
                player.position().add(0, player.getEyeHeight(), 0),
                player.getLookAngle().normalize().scale(spell.getParticleSpeed())
            )
        );
    }
}

package com.magicscience.magicsciencemod.network.magicparticles;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.MagicParticleCreator;
import com.magicscience.magicsciencemod.network.IClientPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;

public class ClientSpawnParticlePacket implements IClientPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull SpellData spellData;
    private final @NotNull Vec3 position;
    private final @NotNull Vec3 direction;

    public ClientSpawnParticlePacket(@NotNull SpellData spellData, @NotNull Vec3 position, @NotNull Vec3 direction) {
        this.spellData = spellData;
        this.position = position;
        this.direction = direction;
    }

    public ClientSpawnParticlePacket(FriendlyByteBuf buf) {
        this.spellData = SpellData.decode(buf);
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buf) {
        this.spellData.encode(buf);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(@NotNull LocalPlayer player) {
        LOGGER.info("Received ClientboundSpawnParticlePacket:");
        LOGGER.info("  Owner ID: {}", spellData.ownerUUID());
        LOGGER.info("  Core ID: {}", spellData.coreId());
        LOGGER.info("  Core Stack: {}", spellData.coreStack());

        LOGGER.info("  Attribute IDs: {}", Arrays.toString(spellData.attributeIds()));
        LOGGER.info("  Attribute Stack: {}", Arrays.toString(spellData.attributeStack()));

        LOGGER.info("  Structure ID: {}", spellData.structureId());
        LOGGER.info("  Structure Stack: {}", spellData.structureStack());

        LOGGER.info("  Particle Speed: {}", spellData.particleSpeed());

        LOGGER.info("  Position: x = {}, y = {}, z = {}", position.x, position.y, position.z);
        LOGGER.info("  Direction: x = {}, y = {}, z = {}", direction.x, direction.y, direction.z);

        var pos = player.position();
        LOGGER.info("  Position: x = {}, y = {}, z = {}", pos.x, pos.y, pos.z);

        new MagicParticleCreator(spellData, position, direction).create();
    }
}

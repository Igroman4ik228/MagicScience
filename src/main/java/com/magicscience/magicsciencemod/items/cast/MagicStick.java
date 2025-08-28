package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.spell.Spell;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellStorage;
import com.magicscience.magicsciencemod.mana.ManaCapabilityHelper;
import com.magicscience.magicsciencemod.network.magicparticles.ClientSpawnParticlePacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class MagicStick extends Item implements ICast {
    private static final Logger LOGGER = LogUtils.getLogger();

    public MagicStick(Item.Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        ItemStack castStack = player.getItemInHand(hand);

        if (hand!=InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(castStack);
        }

        if (player.isCrouching()) {
            SpellStorage.cycleSelectedSpell(castStack, player);
            if (level.isClientSide) {
                player.playSound(SoundEvents.CHAIN_PLACE, 1.0F, 1.0F);
            }
            return InteractionResultHolder.pass(castStack);
        }

        // Get spell just before checking mana and casting
        Spell spell = getSpell(player, castStack);
        LOGGER.debug("Selected spell for cast: {}", SpellConverter.toData(spell));

        int manaCost = spell.getManaCost();
        if (!hasEnoughMana(player, manaCost)) {
            if (level.isClientSide) {
                player.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.5F);
            }
            return InteractionResultHolder.pass(castStack);
        }

        if (!level.isClientSide) {
            ManaCapabilityHelper.removeMana(player, spell.getManaCost());
        }

        if (level.isClientSide && player instanceof LocalPlayer localPlayer) {
            castClient(localPlayer);
        } else if (player instanceof ServerPlayer serverPlayer) {
            castServer(serverPlayer, spell);
        }

        return InteractionResultHolder.sidedSuccess(castStack, level.isClientSide());
    }

    private boolean hasEnoughMana(Player player, int manaCost) {
        if (player.isCreative()) return true;
        return ManaCapabilityHelper.canRemove(player, manaCost);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void castClient(LocalPlayer player) {
        player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
    }

    @Override
    public void castServer(@NotNull ServerPlayer player, @NotNull Spell spell) {
        var spellData = SpellConverter.toData(spell);
        if (!player.getUUID().equals(spellData.ownerUUID())) {
            return;
        }

        LOGGER.info("ServerboundCastParticlePacket");
        LOGGER.info("SpellData received: {}", spellData);

        ModNetwork.CHANNEL.send(
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
            new ClientSpawnParticlePacket(
                spellData,
                player.position().add(0, player.getEyeHeight(), 0),
                player.getLookAngle().normalize().scale(spell.getParticleSpeed())
            )
        );
    }

    @Override
    public Spell getSpell(@NotNull Player player, @NotNull ItemStack castStack) {
        return SpellStorage.getSelectedSpell(castStack, player);
    }
}
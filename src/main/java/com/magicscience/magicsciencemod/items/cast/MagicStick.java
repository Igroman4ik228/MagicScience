package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.factories.MagicAttributeFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicStructureFactory;
import com.magicscience.magicsciencemod.aspects.spell.Spell;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import com.magicscience.magicsciencemod.items.Scroll;
import com.magicscience.magicsciencemod.mana.ManaCapabilityHelper;
import com.magicscience.magicsciencemod.network.magicparticles.ClientSpawnParticlePacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import com.magicscience.magicsciencemod.util.ScrollDataUtils;
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

import java.util.List;

public class MagicStick extends Item implements ICast {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final MagicCoreFactory CORE_FACTORY = new MagicCoreFactory();
    private static final MagicAttributeFactory ATTRIBUTE_FACTORY = new MagicAttributeFactory();
    private static final MagicStructureFactory STRUCTURE_FACTORY = new MagicStructureFactory();

    private Spell spell;

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
        // Check main hand
        if (hand!=InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        //  Общая логика

        setSpell(getSpell(player));

        int manaCost = spell.getManaCost();
        if (!hasEnoughMana(player, manaCost)) {
            if (level.isClientSide) {
                // ToDo: Вынести в client/sound
                // sound cancel cast
                player.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.5F);
            }

            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        if (!level.isClientSide) {
            ManaCapabilityHelper.removeMana(player, spell.getManaCost());
        }

        if (level.isClientSide && player instanceof LocalPlayer localPlayer) {
            castClient(localPlayer);
        } else if (player instanceof ServerPlayer serverPlayer) {
            castServer(serverPlayer);
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    private boolean hasEnoughMana(Player player, int manaCost) {
        if (player.isCreative()) return true;

        return ManaCapabilityHelper.canRemove(player, manaCost);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void castClient(LocalPlayer player) {
        // ToDo: Вынести в client/sound
        player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
    }

    @Override
    public void castServer(
        @NotNull ServerPlayer player
    ) {
        var spellData = SpellConverter.toData(spell);
        // ToDo: i dont know
//        if (!player.getUUID().equals(spellData.ownerUUID()))
//            return;

        LOGGER.info("ServerboundCastParticlePacket");
        LOGGER.info("SpellData received:");
        LOGGER.info("  SpellData: {}", spellData);

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
    public Spell getSpell(@NotNull Player player) {
        // ToDo: It`s dev-code. Edit to relise
        // Check if the player is holding a Scroll in the off-hand
        ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);
        if (offHandStack.getItem() instanceof Scroll) {
            var spellData = ScrollDataUtils.readFromStack(offHandStack);
            if (spellData==null) return spell;
            return SpellConverter.toSpell(spellData);
        } else {
            return new Spell(
                CORE_FACTORY.create(CoreTypes.FIRE, 1),
                List.of(
                    ATTRIBUTE_FACTORY.create(AttributeTypes.SELF_SPECTRE, 1),
                    ATTRIBUTE_FACTORY.create(AttributeTypes.VECTOR, 3)
                ),
                STRUCTURE_FACTORY.create(StructureTypes.SPHERE, 40),
                player.getUUID()
            );
        }
    }

    @Override
    public void setSpell(Spell newSpell) {
        spell = newSpell;
    }
}

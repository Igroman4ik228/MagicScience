package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.factories.MagicAttributeFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicStructureFactory;
import com.magicscience.magicsciencemod.aspects.spell.Spell;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import com.magicscience.magicsciencemod.mana.ManaCapabilityHelper;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundCastParticlePacket;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

public class MagicStick extends Item implements ICast {
    private static final Logger LOGGER = LogUtils.getLogger();

    private Spell spell;

    public MagicStick(Item.Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    @OnlyIn(Dist.CLIENT)
    public InteractionResultHolder<ItemStack> use(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        var coreFactory = new MagicCoreFactory();
        var attributeFactory = new MagicAttributeFactory();
        var structureFactory = new MagicStructureFactory();

        // ToDo: сделать отдельный класс
        // Dynamic create spell
        var spell = new Spell(
            coreFactory.create(CoreTypes.FIRE, 3),
            List.of(
                attributeFactory.create(AttributeTypes.SELF_SPECTRE, 1),
                attributeFactory.create(AttributeTypes.VECTOR, 2)
            ),
            structureFactory.create(StructureTypes.CLOT, 1),
            player.getId()
        );

        setSpell(spell);

        cast(player, spell.getManaCost());

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void cast(Player player, int manaCost) {
        if (!player.isCreative()) {
            int mana = ManaCapabilityHelper.get(player).get().getMana();

            if (mana < manaCost) {
                LOGGER.info("Not enough mana: {}/{}", mana, manaCost);
                player.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.5F); // отказ
                return;
            }
        }

        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundCastParticlePacket(spell)
        );

        // ToDo: Вынести в client/sound
        player.playSound(SoundEvents.FIRECHARGE_USE, 10.0F, 10.0F);
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

    @Override
    public void setSpell(Spell newSpell) {
        spell = newSpell;
    }

}

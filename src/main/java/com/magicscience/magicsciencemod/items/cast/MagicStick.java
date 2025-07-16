package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.spell.Spell;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundCastParticlePacket;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
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

import java.util.List;
import java.util.Objects;

public class MagicStick extends Item implements ICast {
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
        // Check main hand
        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        // ToDo: сделать отдельный класс
        // Dynamic create spell
        var spell = new Spell(
            CoreTypes.getInstance(CoreTypes.FIRE.getId(), 10),     // magicCore
            List.of(
                AttributeTypes.getInstance(AttributeTypes.SELF_SPECTRE.getId()),
                AttributeTypes.getInstance(AttributeTypes.VECTOR.getId())
            ),                                                 // magicAttributes
            null,                                              // magicStructure
            player.getId()                                     // ownerId
        );


        setSpell(spell);

        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundCastParticlePacket(spell)
        );

        // ToDo: Вынести в client/sound
        player.playSound(SoundEvents.FIRECHARGE_USE, 10.0F, 10.0F);

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
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

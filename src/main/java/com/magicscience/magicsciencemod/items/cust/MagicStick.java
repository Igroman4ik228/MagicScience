package com.magicscience.magicsciencemod.items.cust;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.spell.Spell;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collections;

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
        // Check main hand
        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        // ToDo: сделать отдельный класс
        // Dynamic create spell
        var spell = new Spell(
            CoreTypes.getInstance(CoreTypes.FIRE.getId()),
            Collections.singletonList(
                AttributeTypes.getInstance(AttributeTypes.SELF_SPECTRE.getId())
            ),
            player.getId()
        );

        setSpell(spell);

        Vec3 position = player.position().add(0, 1.0, 0);
        Vec3 direction = player.getLookAngle().normalize().scale(this.spell.getParticleSpeed());

        LOGGER.info("stick use!" + "position = " + position + "direction" + direction);


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

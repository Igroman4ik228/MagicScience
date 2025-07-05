package com.magicscience.magicsciencemod.items.cust;

import com.magicscience.magicsciencemod.aspects.Spell;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.SelfSpectreAttribute;
import com.magicscience.magicsciencemod.aspects.cores.FireCore;
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
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        // Проверка ведущей руки
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(player.getItemInHand(hand));

        if (!level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(hand));

        var spell = new Spell(new FireCore(), Collections.singletonList(new SelfSpectreAttribute()), player.getId());

        setSpell(spell);

        Vec3 position = player.position().add(0, 1.0, 0);
        Vec3 direction = player.getLookAngle().normalize().scale(this.spell.getParticleSpeed());

        LOGGER.info("stick use!" + "position = " + position + "direction" + direction);


        // Отправка пакета на сервер
        ModMessagesMagicParticles.CHANNEL.sendToServer(new ServerboundCastParticlePacket(spell));


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

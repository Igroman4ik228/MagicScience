package com.magicscience.magicsciencemod.items.cust;

import com.magicscience.magicsciencemod.aspects.Spell;
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

        if (level.isClientSide) {
            Vec3 position = player.position().add(0, 1.0, 0);
            Vec3 direction = player.getLookAngle().normalize().scale(spell.getParticleSpeed());

            LOGGER.info("stick use!" + "position = " + position + "direction" + direction);

            // Отправка пакета на сервер
            //ModMessagesEnergy.CHANNEL.sendToServer(new ServerboundCastParticlePacket(player.getId(), position, direction, PARTICLE_COUNT, DAMAGE, PARTICLE_RADIUS, true));
        }

        // ToDo: Вынести в client/sound
        player.playSound(SoundEvents.FIRECHARGE_USE, 10.0F, 10.0F);

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public void setSpell(Spell newSpell) {
        spell = newSpell;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }
}

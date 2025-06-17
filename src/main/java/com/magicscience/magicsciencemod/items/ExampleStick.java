package com.magicscience.magicsciencemod.items;

import com.magicscience.magicsciencemod.net.ModMessagesEnergy;
import com.magicscience.magicsciencemod.net.ServerboundCastParticlePacket;
import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ExampleStick extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float DAMAGE = 10.0f;
    private static final float PARTICLE_SPEED = 1f;
    private static final int PARTICLE_COUNT = 300;
    private static final float PARTICLE_RADIUS = 1.5f;

    public ExampleStick(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Проверка ведущей руки
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(player.getItemInHand(hand));

        if (level.isClientSide) {
            Vec3 position = player.position().add(0, 1.0, 0);
            Vec3 direction = player.getLookAngle().normalize().scale(PARTICLE_SPEED);

            LOGGER.info("stick use!");

            // Отправка пакета на сервер
            ModMessagesEnergy.CHANNEL.sendToServer(new ServerboundCastParticlePacket(player.getId(), position, direction, PARTICLE_COUNT, DAMAGE, PARTICLE_RADIUS, true));
        }

        player.playSound(SoundEvents.FIRECHARGE_USE, 10.0F, 10.0F);

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

}

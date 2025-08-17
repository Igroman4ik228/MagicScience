package com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity;

import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.Set;
import java.util.function.Predicate;

public class ConfigEntityFilter implements Predicate<Entity> {
    // ToDo: Вынести в конфиг
    private static final Set<Class<? extends Entity>> EXCLUDED_ENTITIES = Set.of(
        // "Блочные" сущности
        ItemEntity.class,
        FallingBlockEntity.class,
        PrimedTnt.class,
        EndCrystal.class,
        ShulkerBullet.class,
        // Декорации
        ArmorStand.class,
        Painting.class,
        ItemFrame.class,
        LeashFenceKnotEntity.class,
        // Display
        Display.BlockDisplay.class,
        // Прочее
        ExperienceOrb.class,
        Boat.class,
        AbstractMinecart.class
    );

    @Override
    public boolean test(Entity entity) {
        return EXCLUDED_ENTITIES.stream()
            .noneMatch(cls -> cls.isInstance(entity));
    }
}

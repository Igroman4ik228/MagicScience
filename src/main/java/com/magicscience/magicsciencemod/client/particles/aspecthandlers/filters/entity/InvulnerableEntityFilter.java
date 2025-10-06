package com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class InvulnerableEntityFilter implements Predicate<Entity> {
    @Override
    public boolean test(Entity entity) {
        boolean isPlayerCreative = entity instanceof Player player && player.getAbilities().instabuild;

        return entity.isAlive() && !entity.isInvulnerable() && !isPlayerCreative;
    }
}

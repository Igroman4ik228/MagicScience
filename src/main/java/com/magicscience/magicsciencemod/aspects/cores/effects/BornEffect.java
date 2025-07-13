package com.magicscience.magicsciencemod.aspects.cores.effects;

import net.minecraft.world.entity.Entity;

public class BornEffect extends BaseMagicEffect {

    public BornEffect() { }

    public void applyEffect(Entity entity) {
        entity.setSecondsOnFire(5);
    }
}

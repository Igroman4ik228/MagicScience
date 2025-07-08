package com.magicscience.magicsciencemod.aspects.attributes;

import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.function.Predicate;

public class SelfSpectreAttribute extends BaseMagicAttribute implements IFilterMagicAttribute {
    public SelfSpectreAttribute() {
        this(10);
    }

    public SelfSpectreAttribute(int manaCost) {
        super(manaCost);
    }

    @Override
    public Predicate<Entity> getFilteredEntity(Collection<Integer> targetIds) {
        return entity -> !targetIds.contains(entity.getId());
    }
}

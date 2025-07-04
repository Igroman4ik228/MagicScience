package com.magicscience.magicsciencemod.aspects.attributes;

import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public interface IFilterMagicAttribute {
    default Predicate<Entity> getFilteredEntity() {
        return getFilteredEntity(Collections.emptyList());
    }
    public Predicate<Entity> getFilteredEntity(Collection<Integer> targetIds);
}

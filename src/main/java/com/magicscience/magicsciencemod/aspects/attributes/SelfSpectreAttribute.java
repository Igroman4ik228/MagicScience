package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.function.Predicate;

public class SelfSpectreAttribute extends BaseMagicAttribute implements IFilterMagicAttribute {
    public SelfSpectreAttribute(int manaCost) {
        super(manaCost);
    }

    public SelfSpectreAttribute() {
        this(10);
    }

    @Override
    public Predicate<Entity> getFilteredEntity(Collection<Integer> targetIds) {
        return entity -> !targetIds.contains(entity.getId());
    }
}

package com.magicscience.magicsciencemod.aspects.attributes;

import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.function.Predicate;

public class SelfSpectreAttribute extends BaseMagicAttribute implements IFilterMagicAttribute {
    public SelfSpectreAttribute() {
        this(10, 1);
    }

    public SelfSpectreAttribute(int stack) {
        this(10, stack);
    }

    public SelfSpectreAttribute(int manaCost, int stack) {
        super(manaCost, stack);
    }

    @Override
    public Predicate<Entity> getFilteredEntity(Collection<Integer> targetIds) {
        return entity -> !targetIds.contains(entity.getId());
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new VectorAttribute((int) args[0]);
    }
}

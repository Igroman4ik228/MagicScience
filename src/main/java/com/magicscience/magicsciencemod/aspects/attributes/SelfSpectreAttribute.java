package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.function.Predicate;

public class SelfSpectreAttribute extends BaseMagicAttribute implements IFilterMagicAttribute {
    public SelfSpectreAttribute(int manaCost, int stack) {
        super(manaCost, stack);
    }

    public SelfSpectreAttribute() {
        this(10, 1);
    }

    public SelfSpectreAttribute(int stack) {
        this(10, stack);
    }

    @Override
    public Predicate<Entity> getFilteredEntity(Collection<Integer> targetIds) {
        return entity -> !targetIds.contains(entity.getId());
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new SelfSpectreAttribute((int) args[0]);
    }
}

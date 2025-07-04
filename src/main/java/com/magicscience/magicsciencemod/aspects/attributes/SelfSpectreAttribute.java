package com.magicscience.magicsciencemod.aspects.attributes;

import net.minecraft.world.entity.Entity;
import java.util.Collection;
import java.util.function.Predicate;

public class SelfSpectreAttribute implements IMagicAttribute, IFilterMagicAttribute{

    private final int manaCost;
    private final AttributeTypes attributeType;

    public SelfSpectreAttribute() {
        manaCost = 10;
        attributeType = AttributeTypes.SELF_SPECTRE;
    }

    @Override
    public Predicate<Entity> getFilteredEntity(Collection<Integer> targetIds) {
        return entity -> !targetIds.contains(entity.getId());
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public int getTypeCode() {
        return attributeType.getCode();
    }

    @Override
    public AttributeTypes getType() {
        return attributeType;
    }
}

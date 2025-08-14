package com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypeHelper;
import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class AttributeEntityFilter implements Predicate<Entity> {
    private final List<Predicate<Entity>> entityFilters;

    public AttributeEntityFilter(int[] attributeIds) {
        this.entityFilters = IntStream.of(attributeIds)
            .mapToObj(AttributeTypeHelper::findInstance)
            .filter(IFilterMagicAttribute.class::isInstance)
            .map(attr -> ((IFilterMagicAttribute) attr).getEntityFilter())
            .toList();
    }

    @Override
    public boolean test(Entity entity) {
        return entityFilters.stream()
            .allMatch(filter -> filter.test(entity));
    }
}

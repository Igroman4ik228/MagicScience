package com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class EntityFilter implements IEntityFilter {
    private final @NotNull Collection<IEntityFilter> filters;

    public EntityFilter(IEntityFilter... filters) {
        this.filters = List.of(filters);

    }

    @Override
    public boolean test(Entity entity) {
        for (IEntityFilter filter : filters) {
            if (!filter.test(entity)) {
                return false;
            }
        }
        return true;
    }
}

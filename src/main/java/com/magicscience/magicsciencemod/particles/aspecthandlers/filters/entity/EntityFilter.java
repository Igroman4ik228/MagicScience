package com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EntityFilter implements IEntityFilter {
    private final @NotNull Collection<IEntityFilter> filters;

    public EntityFilter(@NotNull Collection<IEntityFilter> filters) {
        this.filters = filters;
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

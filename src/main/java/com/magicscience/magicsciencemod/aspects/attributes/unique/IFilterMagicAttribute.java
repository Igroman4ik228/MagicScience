package com.magicscience.magicsciencemod.aspects.attributes.unique;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Predicate;

public interface IFilterMagicAttribute {
    @NotNull
    default Predicate<Entity> getEntityFilter() {
        return getEntityFilter(Collections.emptyList());
    }

    @NotNull
    Predicate<Entity> getEntityFilter(@NotNull Collection<UUID> targetIds);
}

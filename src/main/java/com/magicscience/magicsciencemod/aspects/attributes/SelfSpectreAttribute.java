package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.attribute.IBaseAttributeConfig;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

public class SelfSpectreAttribute extends BaseMagicAttribute implements IFilterMagicAttribute {
    private static final IBaseAttributeConfig CONFIG = AttributeConfig.get(AttributeTypes.SELF_SPECTRE);

    public SelfSpectreAttribute(@NotNull BaseAttributeData baseAttributeData, int stack) {
        super(baseAttributeData, stack);
    }

    public SelfSpectreAttribute() {
        this(CONFIG.toData(), 1);
    }

    public SelfSpectreAttribute(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    @NotNull
    @OnlyIn(Dist.CLIENT)
    public Predicate<Entity> getEntityFilter(@NotNull Collection<UUID> targetIds) {
        return entity -> !targetIds.contains(entity.getUUID());
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new SelfSpectreAttribute((int) args[0]);
    }
}

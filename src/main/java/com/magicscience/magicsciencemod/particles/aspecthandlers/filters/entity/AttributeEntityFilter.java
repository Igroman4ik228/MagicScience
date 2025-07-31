package com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypeHelper;
import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class AttributeEntityFilter implements IEntityFilter {
    private final SpellData spellData;

    public AttributeEntityFilter(@NotNull SpellData spellData) {
        this.spellData = spellData;
    }

    @Override
    public boolean test(Entity entity) {
        int[] attributeIds = spellData.attributeIds();

        for (int attrId : attributeIds) {
            var attr = AttributeTypeHelper.findInstance(attrId);

            if (attr instanceof IFilterMagicAttribute filterAttr) {
                if (!filterAttr.getEntityFilter().test(entity)) {
                    return false;
                }
            }
        }

        return true;
    }
}

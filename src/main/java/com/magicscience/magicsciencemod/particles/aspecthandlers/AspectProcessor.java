package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.*;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class AspectProcessor {

    private final MagicParticle magicParticle;

    public AspectProcessor(MagicParticle magicParticle) {
        this.magicParticle = magicParticle;
    }

    public void processing() {
        // ToDO:
        //AABB -> pos
        //filter_base
        //filter_attr
        //for loop
        //MP damage
        //Effects?

        AABB particleAABB = calculateAABB();
        Predicate<Entity> filteredEntity = getBaseFilteredEntity()
            .and(getAttributeFilteredEntity());


    }

    private AABB calculateAABB() {
        var currentPosition = magicParticle.getPos();

        var directionPos = magicParticle.getDirectionPos();
        Vec3 nextPosition = currentPosition.add(
            directionPos.x,
            directionPos.y,
            directionPos.z
        );

        // Область поиска коллизи партикла
        return new AABB(currentPosition, nextPosition);
    }

    private Predicate<Entity> getBaseFilteredEntity() {
        return entity -> !(entity instanceof ItemEntity);
    }

    private Predicate<Entity> getAttributeFilteredEntity() {
        // ToDo:
        // pars attr form spellData
        // if attr change filter param -> add to returned filter
        int[] attributeIds = magicParticle.getSpellData().attributeIds();
        int ownerId = magicParticle.getSpellData().ownerId();

        if (attributeIds == null || attributeIds.length == 0) {
            return entity -> true; // нет фильтра
        }

        Predicate<Entity> combined = entity -> true;

        for (int attrId : attributeIds) {
            AttributeTypes attrType = AttributeTypes.fromId(attrId);

            IMagicAttribute attr = switch (attrType) {
                case VECTOR -> new VectorAttribute();
                case SELF_SPECTRE -> new SelfSpectreAttribute();
                case NONE -> null;
            };

            if (attr == null) continue;

            if (attr instanceof IFilterMagicAttribute filterAttr) {
                Predicate<Entity> filter = filterAttr.getFilteredEntity(List.of(ownerId));
                combined = combined.and(filter);
            }
        }

        return combined;
    }
}

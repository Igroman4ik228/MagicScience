package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.IFilterMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.registry.AspectsRegistry;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
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

    public void processing(IMagicCore magicCore) {
        // ToDO:
        //Effects?

        AABB particleAABB = calculateAABB();
        Predicate<Entity> filteredEntity = getBaseFilteredEntity()
            .and(getAttributeFilteredEntity());

        // (Entity) null - все сущности, нет исключений.
        magicParticle.getLevel().getEntities((Entity) null, particleAABB, filteredEntity)
            .forEach(entity -> {
                // Отправка ивента коллизии с entity на сервер
                ModMessagesMagicParticles.CHANNEL.sendToServer(
                    new ServerboundParticleDamagePacket(
                        entity.getId(),
                        magicCore.getDamage(),
                        magicParticle.getSpellData().ownerId()
                    )
                );
                // Удаление партикла
                magicParticle.remove();
            });
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
            var attr = AspectsRegistry.getAttribute(attrId);
            if (attr == null) continue;

            if (attr instanceof IFilterMagicAttribute filterAttr) {
                Predicate<Entity> filter = filterAttr.getFilteredEntity(List.of(ownerId));
                combined = combined.and(filter);
            }
        }

        return combined;
    }
}

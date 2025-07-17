package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.factories.MagicAttributeFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleEffectsPacket;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class AspectProcessor {
    private final @NotNull MagicParticle particle;
    private final @NotNull SpellData spellData;

    private final @NotNull Predicate<Entity> entityFilter;
    private final int damage;

    public AspectProcessor(@NotNull MagicParticle particle) {
        this.particle = particle;
        this.spellData = particle.getSpellData();

        this.entityFilter = getBaseEntityFilter()
            .and(getAttributesEntityFilter());

        var coreFactory = new MagicCoreFactory();

        this.damage = coreFactory.createById(
            spellData.coreId(),
            CoreTypes.class,
            spellData.coreStack())
            .getDamage();
    }

    public void process() {
        // ToDo:
        // ! Stack aspects
        // ? Mana
        // ? Collision with block
        // ! Craft spell
        AABB collisionBox = calculateCollisionBox();

        particle.getLevel()
            .getEntities((Entity) null, collisionBox, entityFilter)
            .forEach(this::handleCollision);
    }

    @NotNull
    private AABB calculateCollisionBox() {
        var currentPosition = particle.getPos();

        var directionPos = particle.getDirectionPos();
        Vec3 nextPosition = currentPosition.add(directionPos);

        return new AABB(currentPosition, nextPosition).inflate(0.1);
    }

    @NotNull
    private Predicate<Entity> getBaseEntityFilter() {
        // ToDo: add blacklist Entity and other MODS
        return entity -> !(entity instanceof ItemEntity);
    }

    @NotNull
    private Predicate<Entity> getAttributesEntityFilter() {
        Predicate<Entity> filter = entity -> true;

        int ownerId = spellData.ownerId();
        int[] attributeIds = spellData.attributeIds();

        var attributeFactory = new MagicAttributeFactory();

        for (int attrId : attributeIds) {
            var attr = attributeFactory.createById(attrId, AttributeTypes.class, 1);

            if (attr instanceof IFilterMagicAttribute filterAttr) {
                filter = filter.and(filterAttr.getFilteredEntity(List.of(ownerId)));
            }
        }

        return filter;
    }

    private void handleCollision(Entity entity) {
        // Send effects
        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundParticleEffectsPacket(
                entity.getId(),
                spellData.coreId()
            )
        );

        // Send damage
        ModMessagesMagicParticles.CHANNEL.sendToServer(
            new ServerboundParticleDamagePacket(
                entity.getId(),
                damage * spellData.coreStack(),
                spellData.ownerId()
            )
        );

        particle.remove();
    }
}

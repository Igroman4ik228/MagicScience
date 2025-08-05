package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleEffectsPacket;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.AttributeEntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.BaseEntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.EntityFilter;
import com.magicscience.magicsciencemod.particles.aspecthandlers.filters.entity.IEntityFilter;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class AspectProcessor {
    private final @NotNull MagicParticle particle;
    private final @NotNull SpellData spellData;

    private final @NotNull IEntityFilter entityFilter;
    private final int damage;

    public AspectProcessor(@NotNull MagicParticle particle) {
        this.particle = particle;
        this.spellData = particle.getSpellData();

        this.entityFilter = new EntityFilter(
            new BaseEntityFilter(),
            new AttributeEntityFilter(spellData)
        );

        var coreFactory = new MagicCoreFactory();
        this.damage = coreFactory.createById(
            spellData.coreId(),
            CoreTypes.class,
            spellData.coreStack()
        ).getDamage();
    }

    public void process() {
        // ToDo:
        // ? Collision with block
        // ! Craft spell

        AABB collisionBox = particle.getBoundingBox();

        particle.getLevel()
            .getEntities((Entity) null, collisionBox, entityFilter)
            .forEach(this::handleCollision);
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
                damage,
                spellData.ownerId()
            )
        );

        particle.remove();
    }
}

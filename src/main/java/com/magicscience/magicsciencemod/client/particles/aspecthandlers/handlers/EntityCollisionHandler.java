package com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers;

import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.network.magicparticles.ServerParticleEntityHitPacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class EntityCollisionHandler extends BaseCollisionHandler<Entity> {
    private static final MagicCoreFactory CORE_FACTORY = new MagicCoreFactory();

    public EntityCollisionHandler(MagicParticle particle, SpellData spellData, Predicate<Entity> blockFilter, Level level) {
        super(particle, spellData, blockFilter, level);
    }

    public void handleCollision() {
        // Particle remove -> just first entity
        level.getEntities((Entity) null, particle.getBoundingBox(), filter)
            .stream()
            .findFirst()
            .ifPresent(this::handleEntityCollision);
    }

    private void handleEntityCollision(Entity entity) {
        var damage = CORE_FACTORY.createById(
            spellData.coreId(),
            spellData.coreStack()
        ).getDamage();

        ModNetwork.CHANNEL.sendToServer(
            new ServerParticleEntityHitPacket(
                entity.getId(),
                spellData.coreId(),
                damage,
                spellData.ownerUUID()
            )
        );

        particle.remove();
    }
}

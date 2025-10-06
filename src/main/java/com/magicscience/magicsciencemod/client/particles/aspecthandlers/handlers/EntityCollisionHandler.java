package com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers;

import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.network.magicparticles.ServerParticleEntityHitPacket;
import com.magicscience.magicsciencemod.registry.ModNetwork;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class EntityCollisionHandler extends BaseCollisionHandler<Entity> {
    private static final MagicCoreFactory CORE_FACTORY = new MagicCoreFactory();

    private final int damage;

    public EntityCollisionHandler(
        @NotNull MagicParticle particle,
        @NotNull SpellData spellData,
        @NotNull Predicate<Entity> entityFilter,
        @NotNull ClientLevel level
    ) {
        super(particle, spellData, entityFilter, level);

        this.damage = CORE_FACTORY.createById(
            spellData.coreId(),
            spellData.coreStack()
        ).getDamage();
    }

    @Override
    public void handleCollision() {
        var entities = level.getEntities((Entity) null, particle.getBoundingBox(), filter);
        if (entities.isEmpty())
            return;

        handleEntityCollision(entities.get(0));
    }

    private void handleEntityCollision(@NotNull Entity entity) {
        ModNetwork.CHANNEL.sendToServer(
            new ServerParticleEntityHitPacket(
                entity.getUUID(),
                spellData.coreId(),
                damage,
                spellData.ownerUUID()
            )
        );

        particle.remove();
    }
}

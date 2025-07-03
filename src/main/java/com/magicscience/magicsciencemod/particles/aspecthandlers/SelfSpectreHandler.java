package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.cores.FireCore;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.net.magicparticles.ServerboundParticleDamagePacket;
import com.magicscience.magicsciencemod.particles.MagicParticle;
import com.magicscience.magicsciencemod.registry.ModMessagesMagicParticles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class SelfSpectreHandler extends BaseAspectHandler {

    private MagicParticle magicParticle;

    public SelfSpectreHandler(MagicParticle magicParticle) {
        super(magicParticle);
    }

    @Override
    protected void collisionEntity() {
        var currentPosition = magicParticle.getPos();
        var directionPos = magicParticle.getDirectionPos();
        Vec3 nextPosition = currentPosition.add(directionPos.x, directionPos.y, directionPos.z);
        AABB particleAABB = new AABB(currentPosition, nextPosition);

        int ownerId = magicParticle.getSpellData().ownerId();

        Predicate<Entity> nonItemEntities = entity ->
                !(entity instanceof ItemEntity) &&
                        entity.getId() != ownerId;

        int coreId = magicParticle.getSpellData().coreId();
        CoreTypes core = CoreTypes.fromCode(coreId);

        IMagicCore magicCore = switch (core) {
            case FIRE -> new FireCore();
        };

        magicParticle.getLevel().getEntities((Entity) null, particleAABB, nonItemEntities)
                .forEach(entity -> {
                    ModMessagesMagicParticles.CHANNEL.sendToServer(
                            new ServerboundParticleDamagePacket(entity.getId(), magicCore.getDamage(), ownerId)
                    );
                    magicParticle.remove();
                });
    }
}

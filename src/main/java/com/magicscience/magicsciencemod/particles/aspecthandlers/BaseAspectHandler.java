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

public abstract class BaseAspectHandler {

    private final MagicParticle magicParticle;

    public BaseAspectHandler(MagicParticle magicParticle) {
        this.magicParticle = magicParticle;
    }

    public void handle() {
        collisionBlock();
        collisionEntity();
    }

    protected void collisionEntity() {
        var currentPosition = magicParticle.getPos();

        var directionPos = magicParticle.getDirectionPos();
        Vec3 nextPosition = currentPosition.add(directionPos.x, directionPos.y, directionPos.z);

        // Область поиска коллизи партикла
        AABB particleAABB = new AABB(currentPosition, nextPosition);

        Predicate<Entity> nonItemEntities = entity -> !(entity instanceof ItemEntity);

        int coreId = magicParticle.getSpellData().coreId();
        CoreTypes core = CoreTypes.fromCode(coreId);

        IMagicCore magicCore = switch (core) {
            case FIRE -> new FireCore();
        };

        // (Entity) null - все сущности, нет исключений.
        magicParticle.getLevel().getEntities((Entity) null, particleAABB, nonItemEntities)
                .forEach(entity -> {

                    // Отправка ивента коллизии с entity на сервер
                    ModMessagesMagicParticles.CHANNEL.sendToServer(
                            new ServerboundParticleDamagePacket(entity.getId(), magicCore.getDamage(), magicParticle.getSpellData().ownerId())
                    );
                    // Удаление партикла
                    magicParticle.remove();
                });
    }

    protected void collisionBlock() {
        return;
    }
}

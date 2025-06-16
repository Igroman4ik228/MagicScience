package com.magicscience.magicsciencemod.particle;

import com.magicscience.magicsciencemod.net.ModMessagesEnergy;
import com.magicscience.magicsciencemod.net.ServerboundParticleCollisionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EnergyParticle extends TextureSheetParticle {

    private final float damage;
    private final int ownerId;

    protected EnergyParticle(ClientLevel level, double x, double y, double z,
                             double dx, double dy, double dz,
                             float damage, int ownerId, SpriteSet sprites) {
        super(level, x, y, z);
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.damage = damage;
        this.ownerId = ownerId;
        this.lifetime = 100;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        // Обработка партиклов за тик на клиенте
        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        Vec3 nextPosition = currentPosition.add(this.xd, this.yd, this.zd);
        // Область поиска коллизи партикла
        AABB particleAABB = new AABB(currentPosition, nextPosition);

        if (!this.level.isClientSide) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        // Фильтр исключения предметов
        Predicate<Entity> nonItemEntities = entity -> !(entity instanceof ItemEntity);
        // (Entity) null - все сущности, нет исключений.
        this.level.getEntities((Entity) null, particleAABB, nonItemEntities)
                .forEach(entity -> {
                    // Отправка ивента коллизии на сервер
                    ModMessagesEnergy.CHANNEL.sendToServer(
                            new ServerboundParticleCollisionPacket(entity.getId(), this.damage, this.ownerId)
                    );
                    // Удаление партикла
                    this.remove();
                });

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;  // Максимальное "свечение" для партикла
    }
}

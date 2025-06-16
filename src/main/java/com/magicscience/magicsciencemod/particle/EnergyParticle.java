package com.magicscience.magicsciencemod.particle;

import com.magicscience.magicsciencemod.net.ModMessagesEnergy;
import com.magicscience.magicsciencemod.net.ServerboundParticleCollisionPacket;
import com.magicscience.magicsciencemod.net.ServerboundPlaceLightPacket;
import com.magicscience.magicsciencemod.net.ServerboundRemoveLightPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;
import java.util.function.Predicate;

public class EnergyParticle extends TextureSheetParticle {

    private static final float DAMAGE = 6.0f;
    private BlockPos lightBlockPos = null;

    private final UUID particleUUID;

    protected EnergyParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x, y, z);
        this.setSpriteFromAge(sprites);
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;

        this.lifetime = 100;

        this.particleUUID = UUID.randomUUID();
    }

    // Обработка партиклов за тик на клиенте
    @Override
    public void tick() {
        if (!this.level.isClientSide) return;

        // Свет
        BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
        if (!pos.equals(lightBlockPos)) {
            lightBlockPos = pos;
            ModMessagesEnergy.CHANNEL.sendToServer(new ServerboundPlaceLightPacket(particleUUID, pos));
        }

        // Коллизия с сущностями
        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        Vec3 nextPosition = currentPosition.add(this.xd, this.yd, this.zd);
        // Область поиска коллизи партикла
        AABB particleAABB = new AABB(currentPosition, nextPosition);

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
                            new ServerboundParticleCollisionPacket(entity.getId(), DAMAGE)
                    );
                    // Удаление партикла
                    this.remove();
                });

        super.tick();
    }

    @Override
    public void remove() {
        if (lightBlockPos != null){
            ModMessagesEnergy.CHANNEL.sendToServer(new ServerboundRemoveLightPacket(particleUUID));
        }

        lightBlockPos = null;
        super.remove();
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
package com.magicscience.magicsciencemod.particle;

import com.magicscience.magicsciencemod.net.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.TntBlock;
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

        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);

        // Удаление партикла если он в воде
        if (this.level.getBlockState(BlockPos.containing(currentPosition)).getFluidState().isSource()) {
            playExtinguishSound(currentPosition);
            this.remove();
        }

        // Свет
        BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
        if (!pos.equals(lightBlockPos)) {
            lightBlockPos = pos;
            ModMessagesEnergy.CHANNEL.sendToServer(new ServerboundPlaceLightPacket(particleUUID, pos));
        }


        Vec3 nextPosition = currentPosition.add(this.xd, this.yd, this.zd);
        // Область поиска коллизи партикла
        AABB particleAABB = new AABB(currentPosition, nextPosition);

        // Коллизия с блоками
        BlockHitResult blockHit = this.level.clip(new ClipContext(
                currentPosition, nextPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHit.getBlockPos();

            // Если блок содержит воду — удаляем партикл
            if (this.level.getBlockState(hitPos).getFluidState().isSource()) {
                playExtinguishSound(hitPos.getCenter());
                this.remove();
            }

            if (this.level.getBlockState(hitPos).getBlock() instanceof TntBlock) {
                ModMessagesEnergy.CHANNEL.sendToServer(
                        new ServerboundParticleBlockCollisionPacket(hitPos)
                );
                this.remove();
            }
        }


        // Коллизия с сущностями
        // Фильтр исключения предметов
        Predicate<Entity> nonItemEntities = entity -> !(entity instanceof ItemEntity);

        // (Entity) null - все сущности, нет исключений.
        this.level.getEntities((Entity) null, particleAABB, nonItemEntities)
                .forEach(entity -> {
                    // Отправка ивента коллизии с entity на сервер
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

    private void playExtinguishSound(Vec3 pos) {
        this.level.playLocalSound(pos.x, pos.y, pos.z,
                SoundEvents.FIRE_EXTINGUISH,
                SoundSource.BLOCKS,
                0.5F,  // громкость
                1.0F,  // питч
                false);
    }
}
package com.magicscience.magicsciencemod.particle;

import com.magicscience.magicsciencemod.net.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;
import java.util.function.Predicate;

public class EnergyParticle extends TextureSheetParticle {

    private final float damage;
    private final int ownerId;
    private final boolean isStick;

    private BlockPos lightBlockPos = null;

    private final UUID particleUUID;

    protected EnergyParticle(ClientLevel level, double x, double y, double z,
                             double dx, double dy, double dz,
                             float damage, int ownerId, boolean isStick, SpriteSet sprites) {
        super(level, x, y, z);
        this.setSpriteFromAge(sprites);
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.damage = damage;
        this.ownerId = ownerId;
        this.isStick = isStick;

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

        if (isStick){
            // Основная позиция партикла
            BlockPos centerPos = BlockPos.containing(this.x, this.y, this.z);

            // Проверяем блоки в радиусе 3 блока под партиклом
            for (int dy = 0; dy <= 3; dy++) {
                BlockPos checkPos = centerPos.below(dy);
                BlockState blockState = level.getBlockState(checkPos);

                // Проверяем только блок травы (GRASS_BLOCK)
                if (blockState.is(Blocks.GRASS_BLOCK)) {
                    BlockPos abovePos = checkPos.above();

                    // Проверяем, что сверху можно что-то посадить
                    if (level.isEmptyBlock(abovePos) ||
                            level.getBlockState(abovePos).is(Blocks.GRASS) ||
                            level.getBlockState(abovePos).is(Blocks.TALL_GRASS)) {

                        // Отправляем пакет на сервер для применения костной муки
                        ModMessagesEnergy.CHANNEL.sendToServer(
                                new ServerboundBoneMealPacket(checkPos)
                        );

                        // Спавним частицы эффекта на клиенте
                        level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                                checkPos.getX() + 0.5,
                                checkPos.getY() + 1,
                                checkPos.getZ() + 0.5,
                                0, 0, 0);
                    }
                }
            }

        }
        else {

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
                                new ServerboundParticleCollisionPacket(entity.getId(), this.damage, this.ownerId)
                        );
                        // Удаление партикла
                        this.remove();
                    });
        }

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

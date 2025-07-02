package com.magicscience.magicsciencemod.particles;

import com.magicscience.magicsciencemod.aspects.SpellData;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import java.util.function.Predicate;

public class MagicParticle extends TextureSheetParticle {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final SpellData spellData;

    public MagicParticle(
            ClientLevel level,
            double x, double y, double z,
            double xd, double yd, double zd,
            SpriteSet sprites,
            SpellData spellData) {
        super(level, x, y, z);

        ResourceLocation particleAtlas = new ResourceLocation("minecraft", "textures/atlas/particles.png");
        ResourceLocation spriteLoc = new ResourceLocation("magicscience", "particle/magic_" + spellData.coreId());

        LOGGER.info("particleAtlas: " + particleAtlas);
        LOGGER.info("spriteLoc: " + spriteLoc);
        LOGGER.info("coreId: " + spellData.coreId());

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(particleAtlas)
                .apply(spriteLoc);

        LOGGER.info("sprite: " + sprite.toString());

        this.setSprite(sprite);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = spellData.particleLifeTime();

        this.spellData = spellData;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tick() {
        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);

        // Удаление партикла если он в воде
        if (this.level.getBlockState(BlockPos.containing(currentPosition)).getFluidState().isSource()) {
            playExtinguishSound(currentPosition);
            this.remove();
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

//            if (this.level.getBlockState(hitPos).getBlock() instanceof TntBlock) {
//                ModMessagesEnergy.CHANNEL.sendToServer(
//                        new ServerboundParticleBlockCollisionPacket(hitPos)
//                );
//                this.remove();
//            }
        }

        // Коллизия с сущностями
        // Фильтр исключения предметов
        Predicate<Entity> nonItemEntities = entity -> !(entity instanceof ItemEntity);

        // (Entity) null - все сущности, нет исключений.
//        this.level.getEntities((Entity) null, particleAABB, nonItemEntities)
//                .forEach(entity -> {
//                    // Отправка ивента коллизии с entity на сервер
//                    ModMessagesEnergy.CHANNEL.sendToServer(
//                            new ServerboundParticleCollisionPacket(entity.getId(), this.damage, this.ownerId)
//                    );
//                    // Удаление партикла
//                    this.remove();
//                });

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
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

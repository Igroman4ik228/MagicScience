package com.magicscience.magicsciencemod.entities;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class MagicEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private @Nullable SpellData spellData = null;

    // Client
    public MagicEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    public MagicEntity(EntityType<? extends Entity> type, Level level, @NotNull SpellData spellData) {
        super(type, level);
        this.spellData = spellData;
    }

    public MagicEntity(EntityType<? extends Entity> type, Level level, @NotNull SpellData spellData, Vec3 pos, Vec3 direction) {
        this(type, level, spellData);
        this.setPos(pos.x, pos.y, pos.z);

        this.setNoGravity(true);
    }

    @Override
    public void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

//        if (level().isClientSide) {
//            LOGGER.info("[Client] MagicEntity Tick | Pos: {}, {}, {} | SpellData: {}",
//                getX(), getY(), getZ(), spellData != null ? spellData : "null");
//        } else {
//            LOGGER.info("[Server] MagicEntity Tick | Pos: {}, {}, {} | SpellData: {}",
//                getX(), getY(), getZ(), spellData != null ? spellData : "null");
//        }

    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096.0D;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false; // отключает возможность выделения
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}

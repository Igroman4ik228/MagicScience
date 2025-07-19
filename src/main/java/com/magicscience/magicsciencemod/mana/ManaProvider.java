package com.magicscience.magicsciencemod.mana;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.registry.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaProvider implements ICapabilitySerializable<Tag> {
    public static final ResourceLocation ID = new ResourceLocation(MagicScienceMod.MOD_ID, "mana");

    private final Mana backend = new Mana();
    private final LazyOptional<IMana> optional = LazyOptional.of(() -> backend);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ModCapabilities.MANA_CAPABILITY.orEmpty(cap, optional);
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("mana", backend.getMana());
        tag.putInt("maxMana", backend.getMaxMana());
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag tag = (CompoundTag) nbt;
        backend.setMana(tag.getInt("mana"));
        backend.setMaxMana(tag.getInt("maxMana"));
    }

    public void invalidate() {
        optional.invalidate();
    }
}


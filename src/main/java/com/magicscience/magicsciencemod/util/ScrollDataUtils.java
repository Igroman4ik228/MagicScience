package com.magicscience.magicsciencemod.util;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.stream.IntStream;

public final class ScrollDataUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String kOwnerUUID = "OwnerUUID";
    private static final String kCoreId = "CoreId";
    private static final String kCoreStack = "CoreStack";
    private static final String kAttrIds = "AttributeIds";
    private static final String kAttrStacks = "AttributeStacks";
    private static final String kStructId = "StructureId";
    private static final String kStructStack = "StructureStack";
    private static final String kParticleSpeed = "ParticleSpeed";
    private static final String kParticleLife = "ParticleLifeTime";

    private ScrollDataUtils() {
    }

    public static void writeToStack(ItemStack stack, SpellData d) {
        CompoundTag t = stack.getOrCreateTag();
        t.putUUID(kOwnerUUID, d.ownerUUID());
        t.putInt(kCoreId, d.coreId());
        t.putInt(kCoreStack, d.coreStack());
        t.putIntArray(kAttrIds, d.attributeIds());
        t.putIntArray(kAttrStacks, d.attributeStack());
        t.putInt(kStructId, d.structureId());
        t.putInt(kStructStack, d.structureStack());
        t.putInt(kParticleSpeed, d.particleSpeed());
        t.putInt(kParticleLife, d.particleLifeTime());
    }

    public static SpellData readFromStack(ItemStack stack) {
        CompoundTag t = stack.getTag();
        if (t==null) {
            LOGGER.info("No NBT tag found for ItemStack: {}", stack);
            return null;
        }
        
        return new SpellData(
            t.getUUID(kOwnerUUID),
            t.getInt(kCoreId),
            t.getInt(kCoreStack),
            getIntArrayFromTag(t, kAttrIds),
            getIntArrayFromTag(t, kAttrStacks),
            t.getInt(kStructId),
            t.getInt(kStructStack),
            t.getInt(kParticleSpeed),
            t.getInt(kParticleLife)
        );
    }

    /**
     * kAttrIds and kAttrStacks can be LIST - command create (/give)
     * kAttrIds and kAttrStacks can be INT[] - program create
     * This method for get array from all correct datatype
     */
    private static int[] getIntArrayFromTag(CompoundTag tag, String key) {
        Tag nbtTag = tag.get(key);
        if (nbtTag instanceof IntArrayTag) {
            return tag.getIntArray(key);
        }
        if (nbtTag instanceof ListTag listTag && listTag.getElementType()==Tag.TAG_INT) {
            return IntStream.range(0, listTag.size())
                .map(listTag::getInt)
                .toArray();
        }
        LOGGER.warn("Invalid or missing tag for key {}: {}", key, nbtTag==null ? "null":nbtTag.getType().getName());
        return new int[0];
    }
}


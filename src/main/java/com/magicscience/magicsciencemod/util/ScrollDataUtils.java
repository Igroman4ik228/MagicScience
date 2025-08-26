package com.magicscience.magicsciencemod.util;

import com.magicscience.magicsciencemod.items.ScrollData;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.stream.IntStream;

public final class ScrollDataUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String kAuthorUUID = "AuthorUUID";
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

    public static void writeSpellData(ScrollData data, Object target) {
        CompoundTag tag = target instanceof ItemStack stack
            ? stack.getOrCreateTag()
            :(CompoundTag) target;

        if (data.authorUUID()!=null) {
            tag.putUUID(kAuthorUUID, data.authorUUID());
        }
        tag.putInt(kCoreId, data.coreId());
        tag.putInt(kCoreStack, data.coreStack());
        tag.put(kAttrIds, new IntArrayTag(data.attributeIds()));
        tag.put(kAttrStacks, new IntArrayTag(data.attributeStack()));
        tag.putInt(kStructId, data.structureId());
        tag.putInt(kStructStack, data.structureStack());
        tag.putInt(kParticleSpeed, data.particleSpeed());
        tag.putInt(kParticleLife, data.particleLifeTime());
    }

    public static ScrollData readSpellData(Object source) {
        CompoundTag tag = source instanceof ItemStack stack
            ? stack.getTag()
            :(CompoundTag) source;

        if (tag==null) {
            LOGGER.info("No NBT tag found for source: {}", source);
            return null;
        }

        UUID authorUUID = null;
        if (tag.contains(kAuthorUUID)) {
            authorUUID = tag.getUUID(kAuthorUUID);
        }

        return new ScrollData(
            authorUUID,
            tag.getInt(kCoreId),
            tag.getInt(kCoreStack),
            getIntArrayFromTag(tag, kAttrIds),
            getIntArrayFromTag(tag, kAttrStacks),
            tag.getInt(kStructId),
            tag.getInt(kStructStack),
            tag.getInt(kParticleSpeed),
            tag.getInt(kParticleLife)
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


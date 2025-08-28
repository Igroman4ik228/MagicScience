package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.factories.MagicAttributeFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicStructureFactory;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import com.magicscience.magicsciencemod.items.Scroll;
import com.magicscience.magicsciencemod.items.ScrollData;
import com.magicscience.magicsciencemod.util.ScrollDataUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellStorage {
    private static final String NBT_SPELL_LIST = "SpellList";
    private static final String NBT_SELECTED_INDEX = "SelectedSpellIndex";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final MagicCoreFactory CORE_FACTORY = new MagicCoreFactory();
    private static final MagicAttributeFactory ATTRIBUTE_FACTORY = new MagicAttributeFactory();
    private static final MagicStructureFactory STRUCTURE_FACTORY = new MagicStructureFactory();

    /**
     * Gets the selected spell from the item's spell list or default if none is valid.
     */
    public static Spell getSelectedSpell(@NotNull ItemStack castStack, @NotNull Player player) {
        updateSpellList(castStack, player);
        List<ScrollData> spellList = readSpellList(castStack);
        int selectedIndex = getSelectedIndex(castStack);

        LOGGER.debug("Getting selected spell: index={}, listSize={}", selectedIndex, spellList.size());

        if (selectedIndex >= 0 && selectedIndex < spellList.size()) {
            return SpellConverter.toSpell(spellList.get(selectedIndex), player.getUUID());
        }

        setSelectedIndex(castStack, -1);
        LOGGER.debug("No valid spell selected, using default spell");
        return getDefaultSpell(player);
    }

    /**
     * Switches to the next spell in the list.
     */
    public static void cycleSelectedSpell(@NotNull ItemStack castStack, @NotNull Player player) {
        updateSpellList(castStack, player);
        List<ScrollData> spellList = readSpellList(castStack);
        if (spellList.isEmpty()) {
            setSelectedIndex(castStack, -1);
            LOGGER.debug("Spell list empty, setting index to -1");
            return;
        }

        int currentIndex = getSelectedIndex(castStack);
        int newIndex = (currentIndex + 1) % spellList.size();
        setSelectedIndex(castStack, newIndex);
        LOGGER.debug("Cycling spell: oldIndex={}, newIndex={}, listSize={}", currentIndex, newIndex, spellList.size());
    }

    /**
     * Updates the spell list in the item's NBT based on scrolls in the player's inventory.
     */
    private static void updateSpellList(@NotNull ItemStack castStack, @NotNull Player player) {
        List<ScrollData> newList = collectSpellsFromInventory(player);
        ScrollData selectedSpell = getCurrentSelectedSpell(castStack);
        saveSpellList(castStack, newList);
        updateSelectedIndex(castStack, newList, selectedSpell);
        LOGGER.debug("Updated spell list: size={}", newList.size());
    }

    /**
     * Collects unique spells from scrolls in the player's inventory.
     */
    private static List<ScrollData> collectSpellsFromInventory(@NotNull Player player) {
        List<ScrollData> spells = new ArrayList<>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof Scroll) {
                ScrollData data = ScrollDataUtils.readScrollData(stack);
                if (data!=null && !spells.contains(data)) {
                    spells.add(data);
                    LOGGER.debug("Added spell from inventory slot {}: {}", i, data);
                }
            }
        }
        return spells;
    }

    /**
     * Gets the currently selected spell from the item's NBT, if any.
     */
    private static ScrollData getCurrentSelectedSpell(@NotNull ItemStack castStack) {
        List<ScrollData> spellList = readSpellList(castStack);
        int selectedIndex = getSelectedIndex(castStack);
        if (selectedIndex >= 0 && selectedIndex < spellList.size()) {
            return spellList.get(selectedIndex);
        }
        return null;
    }

    /**
     * Saves the spell list to the item's NBT.
     */
    private static void saveSpellList(@NotNull ItemStack stack, @NotNull List<ScrollData> spellList) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listTag = new ListTag();
        for (ScrollData data : spellList) {
            CompoundTag spellTag = new CompoundTag();
            ScrollDataUtils.writeScrollData(data, spellTag);
            listTag.add(spellTag);
        }
        tag.put(NBT_SPELL_LIST, listTag);
    }

    /**
     * Updates the selected spell index based on the new list and previous selection.
     */
    private static void updateSelectedIndex(@NotNull ItemStack castStack, @NotNull List<ScrollData> newList, ScrollData selectedSpell) {
        int currentIndex = getSelectedIndex(castStack);
        if (selectedSpell!=null && currentIndex >= 0 && currentIndex < newList.size() && Objects.equals(newList.get(currentIndex), selectedSpell)) {
            // Current index is still valid, no need to change
            LOGGER.debug("Preserving current index: {}", currentIndex);
            return;
        }

        if (selectedSpell!=null) {
            for (int i = 0; i < newList.size(); i++) {
                if (Objects.equals(newList.get(i), selectedSpell)) {
                    setSelectedIndex(castStack, i);
                    LOGGER.debug("Found matching spell, setting index to {}", i);
                    return;
                }
            }
        }

        setSelectedIndex(castStack, newList.isEmpty() ? -1:0);
        LOGGER.debug("Resetting index to {} (list empty: {})", newList.isEmpty() ? -1:0, newList.isEmpty());
    }

    /**
     * Reads the spell list from the item's NBT.
     */
    private static List<ScrollData> readSpellList(@NotNull ItemStack stack) {
        List<ScrollData> list = new ArrayList<>();
        if (!stack.hasTag()) return list;
        CompoundTag tag = stack.getTag();
        if (!tag.contains(NBT_SPELL_LIST)) return list;
        ListTag listTag = tag.getList(NBT_SPELL_LIST, Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            ScrollData data = ScrollDataUtils.readScrollData(listTag.getCompound(i));
            if (data!=null) {
                list.add(data);
            }
        }
        return list;
    }

    /**
     * Gets the selected spell index from the item's NBT.
     */
    private static int getSelectedIndex(@NotNull ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(NBT_SELECTED_INDEX)
            ? stack.getTag().getInt(NBT_SELECTED_INDEX)
            :-1;
    }

    /**
     * Sets the selected spell index in the item's NBT.
     */
    private static void setSelectedIndex(@NotNull ItemStack stack, int index) {
        CompoundTag tag = stack.getOrCreateTag();
        if (index < 0) {
            tag.remove(NBT_SELECTED_INDEX);
        } else {
            tag.putInt(NBT_SELECTED_INDEX, index);
        }
    }

    /**
     * Returns the default spell for the player.
     */
    private static Spell getDefaultSpell(@NotNull Player player) {
        return new Spell(
            CORE_FACTORY.create(CoreTypes.FIRE, 1),
            List.of(
                ATTRIBUTE_FACTORY.create(AttributeTypes.SELF_SPECTRE, 1),
                ATTRIBUTE_FACTORY.create(AttributeTypes.VECTOR, 1)
            ),
            STRUCTURE_FACTORY.create(StructureTypes.SPHERE, 3),
            player.getUUID()
        );
    }
}
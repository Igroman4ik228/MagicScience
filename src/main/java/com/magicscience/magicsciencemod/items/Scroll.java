package com.magicscience.magicsciencemod.items;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypeHelper;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypeHelper;
import com.magicscience.magicsciencemod.util.ScrollDataUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class Scroll extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    public Scroll(Properties properties) {
        super(properties);
    }

    /**
     * Удобный фабричный метод: создаёт стек свитка с уже записанным SpellData
     */
    public static ItemStack of(SpellData data, Item item) {
        ItemStack stack = new ItemStack(item);
        var scrollData = SpellConverter.toScroll(data);
        ScrollDataUtils.writeSpellData(scrollData, stack);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ScrollData data = ScrollDataUtils.readSpellData(stack);
        if (data==null) {
            tooltip.add(Component.translatable("tooltip.magicscience.scroll.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        // Core
        IMagicCore core = CoreTypeHelper.findInstance(data.coreId());
        tooltip.add(Component.translatable(core.getTranslationKey())
            .append(" x" + data.coreStack())
            .withStyle(ChatFormatting.GOLD));

        // Attributes
        for (int i = 0; i < data.attributeIds().length; i++) {
            int attrId = data.attributeIds()[i];
            IMagicAttribute attr = AttributeTypeHelper.findInstance(attrId);

            if (attr!=null) {
                tooltip.add(Component.translatable(attr.getTranslationKey())
                    .append(" x" + data.attributeStack()[i])
                    .withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.literal("No attr: " + attrId)
                    .withStyle(ChatFormatting.RED));
            }
        }

        // Structure
        IMagicStructure structure = StructureTypeHelper.findInstance(data.structureId());
        tooltip.add(Component.translatable(structure.getTranslationKey())
            .append(" x" + data.structureStack())
            .withStyle(ChatFormatting.DARK_GREEN));
    }
}
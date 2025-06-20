package com.magicscience.magicsciencemod.screen;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ToolSlot extends SlotItemHandler {
    public ToolSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(net.minecraft.world.item.Items.DIAMOND_SWORD) || stack.is(net.minecraft.world.item.Items.STICK);
    }
}
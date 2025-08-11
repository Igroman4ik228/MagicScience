package com.magicscience.magicsciencemod.client.menu;

import com.magicscience.magicsciencemod.blocks.entity.MagicWorkbenchBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MagicWorkbenchMenu extends AbstractContainerMenu {
    private final MagicWorkbenchBlockEntity blockEntity;

    public MagicWorkbenchMenu(int id, Inventory playerInventory, MagicWorkbenchBlockEntity blockEntity) {
        super(ModMenuTypes.MAGIC_WORKBENCH.get(), id);
        this.blockEntity = blockEntity;
        IItemHandler handler = blockEntity.getItemHandler();

        // Добавление слотов рабочего стола
        this.addSlot(new SlotItemHandler(handler, 0, 44, 17)); // Core
        this.addSlot(new SlotItemHandler(handler, 1, 62, 17)); // Attribute
        this.addSlot(new SlotItemHandler(handler, 2, 80, 17)); // Structure
        this.addSlot(new SlotItemHandler(handler, 3, 98, 17)); // Magic Ink
        this.addSlot(new SlotItemHandler(handler, 4, 116, 17)); // Paper
        this.addSlot(new SlotItemHandler(handler, 5, 134, 17)); // Result

        // Добавление слотов инвентаря игрока — стандартно
        // (27 слотов инвентаря + 9 слотов быстрого доступа)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 50 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 108));
        }
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.blockEntity != null && this.blockEntity.getLevel().getBlockEntity(this.blockEntity.getBlockPos()) == this.blockEntity;
    }
}

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
        this.addSlot(new SlotItemHandler(handler, 0, 8, 9));   // Core
        this.addSlot(new SlotItemHandler(handler, 1, 8, 32));  // Attribute
        this.addSlot(new SlotItemHandler(handler, 2, 8, 55));  // Structure
        this.addSlot(new SlotItemHandler(handler, 3, 116, 58));  // Magic Ink
        this.addSlot(new SlotItemHandler(handler, 4, 79, 42));  // Paper
        this.addSlot(new SlotItemHandler(handler, 5, 151, 32));  // Result

        // Добавление слотов инвентаря игрока — стандартно
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18)); // y=84, 102, 120
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 142)); // y=142
        }
    }

    public MagicWorkbenchBlockEntity getBlockEntity() {
        return blockEntity;
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

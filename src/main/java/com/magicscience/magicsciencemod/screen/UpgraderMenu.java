package com.magicscience.magicsciencemod.screen;

import com.magicscience.magicsciencemod.blocks.ModBlocks;
import com.magicscience.magicsciencemod.blocks.entity.UpgraderBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class UpgraderMenu extends AbstractContainerMenu {
    public final UpgraderBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private final IItemHandler playerInventory;

    public UpgraderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public UpgraderMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.UPGRADER_MENU.get(), id);
        blockEntity = (UpgraderBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;
        this.playerInventory = new InvWrapper(inv);

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        if(blockEntity != null) {
            // Используем напрямую ItemStackHandler из BlockEntity
            this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 0, 56, 17)); // Слот для меча/палки
            this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 1, 56, 53)); // Слот для улучшаемого предмета
            this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 2, 8, 53));  // Слот для топлива
            this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 3, 116, 35)); // Слот для результата
        }

        addDataSlots(data);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < 4) { // Если это слоты блока
                if (!this.moveItemStackTo(stack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
            } else { // Если это слоты инвентаря игрока
                if (!this.moveItemStackTo(stack, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.UPGRADER.get());
    }

    public int getEnergy() {
        return data.get(0);
    }

    public int getMaxEnergy() {
        return data.get(1);
    }
}
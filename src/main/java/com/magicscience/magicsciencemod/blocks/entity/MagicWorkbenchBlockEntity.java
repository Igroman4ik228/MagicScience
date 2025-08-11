package com.magicscience.magicsciencemod.blocks.entity;

import com.magicscience.magicsciencemod.client.menu.MagicWorkbenchMenu;
import com.magicscience.magicsciencemod.registry.ModBlockEntities;
import com.magicscience.magicsciencemod.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagicWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // Core
            if (slot == 0) {
                return true; // любой предмет, можно позже ограничить
            }
            // Attribute
            if (slot == 1) {
                return true;
            }
            // Structure
            if (slot == 2) {
                return true;
            }
            // Magic Ink
            if (slot == 3) {
                return stack.is(ModItems.MAGIC_INK.get());
            }
            // Paper
            if (slot == 4) {
                return stack.is(Items.PAPER);
            }
            // Result — запрещаем ручную установку
            if (slot == 5) {
                return false;
            }
            return true;
        }
    };

    private final LazyOptional<IItemHandler> handlerOptional = LazyOptional.of(() -> itemHandler);

    public MagicWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_WORKBENCH.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magicscience.magic_workbench");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new MagicWorkbenchMenu(id, playerInventory, this);
    }


    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("Inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handlerOptional.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return handlerOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}


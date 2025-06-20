package com.magicscience.magicsciencemod.blocks.entity;

import com.magicscience.magicsciencemod.blocks.ModBlocks;

import com.magicscience.magicsciencemod.items.ModItems;
import com.magicscience.magicsciencemod.screen.UpgraderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class UpgraderBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide() && (slot == 0 || slot == 1 || slot == 2)) {
                craftItem();
            }
        }
    };

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energy;
                case 1 -> maxEnergy;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> energy = value;
                case 1 -> maxEnergy = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private int energy = 0;
    private int maxEnergy = 1000;

    public UpgraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UPGRADER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UpgraderBlockEntity entity) {
        if (level.isClientSide()) return;

        if (hasFuelInFuelSlot(entity) && entity.energy < entity.maxEnergy) {
            entity.energy += 100;
            entity.itemHandler.extractItem(2, 1, false);
            setChanged(level, pos, state);
        }

        entity.craftItem();
    }

    private static boolean hasFuelInFuelSlot(UpgraderBlockEntity entity) {
        ItemStack stack = entity.itemHandler.getStackInSlot(2);
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    private void craftItem() {
        if (energy < maxEnergy) return;

        ItemStack tool = itemHandler.getStackInSlot(0);
        ItemStack upgrade = itemHandler.getStackInSlot(1);
        ItemStack result = itemHandler.getStackInSlot(3);

        // Проверка на невозможность добавить результат
        boolean canOutputSword = result.isEmpty() || (result.is(ModItems.EXAMPLE_SWORD.get()) && result.getCount() < result.getMaxStackSize());
        boolean canOutputStick = result.isEmpty() || (result.is(ModItems.EXAMPLE_STICK.get()) && result.getCount() < result.getMaxStackSize());

        // Проверка для example_sword
        if (tool.is(Items.DIAMOND_SWORD) && upgrade.is(Items.GLOWSTONE_DUST)
                && tool.getCount() >= 1 && upgrade.getCount() >= 30
                && canOutputSword) {

            itemHandler.extractItem(0, 1, false);
            itemHandler.extractItem(1, 30, false);

            if (result.isEmpty()) {
                itemHandler.setStackInSlot(3, new ItemStack(ModItems.EXAMPLE_SWORD.get()));
            } else {
                result.grow(1);
            }

            energy = 0;
        }

        // Проверка для example_stick
        else if (tool.is(Items.STICK) && upgrade.is(Items.BONE_MEAL)
                && tool.getCount() >= 1 && upgrade.getCount() >= 30
                && hasConnectedBlock() && canOutputStick) {

            itemHandler.extractItem(0, 1, false);
            itemHandler.extractItem(1, 30, false);

            if (result.isEmpty()) {
                itemHandler.setStackInSlot(3, new ItemStack(ModItems.EXAMPLE_STICK.get()));
            } else {
                result.grow(1);
            }

            energy = 0;
        }
    }

    private boolean hasConnectedBlock() {
        return isStickCrafterAt(worldPosition.above()) ||
                isStickCrafterAt(worldPosition.below()) ||
                isStickCrafterAt(worldPosition.north()) ||
                isStickCrafterAt(worldPosition.south()) ||
                isStickCrafterAt(worldPosition.west()) ||
                isStickCrafterAt(worldPosition.east());
    }

    private boolean isStickCrafterAt(BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.STICK_CRAFTER.get());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magicscience.upgrader");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new UpgraderMenu(containerId, inventory, this, data);
    }
}
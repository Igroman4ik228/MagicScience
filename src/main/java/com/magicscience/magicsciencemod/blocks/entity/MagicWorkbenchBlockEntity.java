package com.magicscience.magicsciencemod.blocks.entity;

import com.magicscience.magicsciencemod.client.menu.MagicWorkbenchMenu;
import com.magicscience.magicsciencemod.registry.ModBlockEntities;
import com.magicscience.magicsciencemod.registry.ModItems;
import com.mojang.logging.LogUtils;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class MagicWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MAX_INK_LEVEL = 1000;    // Максимальная ёмкость уровня чернил
    private static final int INK_PER_ITEM = 10;       // Количество чернил за одну единицу MAGIC_INK
    private static final int TICKS_PER_INK = 20;      // Тиков, необходимых для обработки 1 единицы чернил (20 тиков = 1 секунда)

    private int inkLevel = 0;                         // Текущий уровень чернил
    private int inkProcessingTicks = 0;               // Счётчик тиков для обработки
    private int inkToProcess = 0;                     // Общее количество чернил, ожидающих обработки

    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot == 3 && level != null && !level.isClientSide) {
                startInkProcessing();
                LOGGER.info("Ink processing started at pos {} with inkToProcess: {}", getBlockPos(), inkToProcess);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // Слот для ядра
            if (slot == 0) {
                return true;
            }
            // Слот для атрибута
            if (slot == 1) {
                return true;
            }
            // Слот для структуры
            if (slot == 2) {
                return true;
            }
            // Слот для магических чернил
            if (slot == 3) {
                return stack.is(ModItems.MAGIC_INK.get());
            }
            // Слот для бумаги
            if (slot == 4) {
                return stack.is(Items.PAPER);
            }
            // Слот для результата
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

    // Метод для запуска обработки чернил
    private void startInkProcessing() {
        ItemStack inkStack = itemHandler.getStackInSlot(3);
        if (!inkStack.isEmpty() && inkStack.is(ModItems.MAGIC_INK.get())) {
            inkToProcess = inkStack.getCount() * INK_PER_ITEM; // Общее количество чернил для обработки
            inkProcessingTicks = 0; // Сброс счётчика тиков при старте обработки
            LOGGER.info("Setting inkToProcess to {} and resetting ticks to 0 at pos {}", inkToProcess, getBlockPos());
            setChanged();
        } else {
            LOGGER.info("No valid ink stack found at pos {}", getBlockPos());
        }
    }

    // Упрощённый метод серверного тика
    public void serverTick(Level level, BlockPos pos, BlockState state, MagicWorkbenchBlockEntity blockEntity) {
        LOGGER.info("Server tick called at pos {} with inkToProcess: {}", pos, inkToProcess);
        if (inkToProcess <= 0) {
            LOGGER.info("No ink to process at pos {}, exiting.", pos);
            return; // Обработка не требуется
        }
        LOGGER.info("Starting ink processing at pos {}", pos);

        processInkTick(blockEntity);
        updateInkStackIfComplete(blockEntity);

        LOGGER.info("Server tick ended at pos {} with inkLevel: {}, inkToProcess: {}", pos, inkLevel, inkToProcess);
    }

    // Обработка одного тика чернил
    private void processInkTick(MagicWorkbenchBlockEntity blockEntity) {
        LOGGER.info("Processing ink tick at pos {}, current ticks: {}", blockEntity.getBlockPos(), inkProcessingTicks);
        if (inkProcessingTicks >= TICKS_PER_INK) {
            int inkToAdd = Math.min(INK_PER_ITEM, inkToProcess);
            LOGGER.info("Adding {} ink at pos {}", inkToAdd, blockEntity.getBlockPos());
            addInkAndUpdate(inkToAdd);
            inkProcessingTicks = 0; // Сброс таймера
        } else {
            inkProcessingTicks++; // Увеличение счётчика тиков
            LOGGER.info("Incrementing ticks to {} at pos {}", inkProcessingTicks, blockEntity.getBlockPos());
        }
    }

    // Добавление чернил и обновление состояния
    private void addInkAndUpdate(int inkToAdd) {
        LOGGER.info("Attempting to add {} ink, current level: {} at pos {}", inkToAdd, inkLevel, getBlockPos());
        if (inkLevel + inkToAdd <= MAX_INK_LEVEL) {
            inkLevel += inkToAdd;
            inkToProcess -= inkToAdd;
            LOGGER.info("Added {} ink, new level: {}, remaining to process: {}", inkToAdd, inkLevel, inkToProcess);
        } else {
            int availableSpace = MAX_INK_LEVEL - inkLevel;
            inkLevel = MAX_INK_LEVEL;
            inkToProcess -= availableSpace;
            LOGGER.info("Added {} ink (limited by max), new level: {}, remaining to process: {}", availableSpace, inkLevel, inkToProcess);
        }
        setChanged();
    }

    // Обновление стека чернил в слоте при завершении обработки
    private void updateInkStackIfComplete(MagicWorkbenchBlockEntity blockEntity) {
        if (inkToProcess == 0) {
            LOGGER.info("Processing complete at pos {}, updating ink stack.", blockEntity.getBlockPos());
            ItemStack inkStack = itemHandler.getStackInSlot(3);
            if (!inkStack.isEmpty()) {
                int itemsProcessed = (int) Math.ceil((double) (inkStack.getCount() * INK_PER_ITEM - inkToProcess) / INK_PER_ITEM);
                LOGGER.info("Shrinking ink stack by {} items at pos {}", itemsProcessed, blockEntity.getBlockPos());
                inkStack.shrink(itemsProcessed);
                if (inkStack.isEmpty()) {
                    itemHandler.setStackInSlot(3, ItemStack.EMPTY);
                    LOGGER.info("Ink stack cleared at pos {}", blockEntity.getBlockPos());
                }
            } else {
                LOGGER.info("Ink stack already empty at pos {}", blockEntity.getBlockPos());
            }
        }
    }

    public int getInkLevel() {
        return inkLevel;
    }

    public int getMaxInkLevel() {
        return MAX_INK_LEVEL;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
        inkLevel = nbt.getInt("InkLevel");
        inkToProcess = nbt.getInt("InkToProcess");
        inkProcessingTicks = nbt.getInt("InkProcessingTicks");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("Inventory", itemHandler.serializeNBT());
        nbt.putInt("InkLevel", inkLevel);
        nbt.putInt("InkToProcess", inkToProcess);
        nbt.putInt("InkProcessingTicks", inkProcessingTicks);
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
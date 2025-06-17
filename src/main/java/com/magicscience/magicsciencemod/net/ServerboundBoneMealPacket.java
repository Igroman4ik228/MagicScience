package com.magicscience.magicsciencemod.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ServerboundBoneMealPacket {
    private final BlockPos pos;

    public ServerboundBoneMealPacket(BlockPos pos) {
        this.pos = pos;
    }

    public ServerboundBoneMealPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            BlockState blockState = level.getBlockState(pos);

            // Проверяем ТОЛЬКО блок травы (GRASS_BLOCK)
            if (blockState.is(Blocks.GRASS_BLOCK)) {
                ItemStack boneMealStack = new ItemStack(Items.BONE_MEAL);

                // Применяем только если сверху есть место для роста
                BlockPos abovePos = pos.above();
                if (level.isEmptyBlock(abovePos) ||
                        level.getBlockState(abovePos).is(Blocks.GRASS) ||
                        level.getBlockState(abovePos).is(Blocks.TALL_GRASS)) {

                    if (BoneMealItem.applyBonemeal(boneMealStack, level, pos, player)) {
                        level.playSound(
                                null,
                                pos,
                                SoundEvents.BONE_MEAL_USE,
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F
                        );
                        level.levelEvent(2005, pos, 0);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

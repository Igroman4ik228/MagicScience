package com.magicscience.magicsciencemod.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundParticleBlockCollisionPacket {
    private final BlockPos pos;

    public ServerboundParticleBlockCollisionPacket(BlockPos pos) {
        this.pos = pos;
    }

    public ServerboundParticleBlockCollisionPacket(FriendlyByteBuf buf) {
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
            if (level.getBlockState(pos).getBlock() instanceof TntBlock) {
                // Удаляем блок TNT
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                // Создаём активированный TNT
                PrimedTnt primedTnt = new PrimedTnt(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player);
                level.addFreshEntity(primedTnt);

                // Воспроизводим звук поджога TNT
                level.playSound(
                        null,                  // null = слышат все игроки вокруг
                        pos,                   // позиция
                        SoundEvents.TNT_PRIMED,// нужный звук
                        SoundSource.BLOCKS,    // категория
                        1.0F,                  // громкость
                        1.0F                   // питч
                );

            }
        });
        ctx.get().setPacketHandled(true);
    }
}

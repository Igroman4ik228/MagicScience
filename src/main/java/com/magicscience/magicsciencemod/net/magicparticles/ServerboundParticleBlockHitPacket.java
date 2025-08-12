package com.magicscience.magicsciencemod.net.magicparticles;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ServerboundParticleBlockHitPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final BlockPos pos;
    private final int face;
    private final double hitX, hitY, hitZ;
    private final CompoundTag extra; // содержит coreId и ownerId и другие свойства

    public ServerboundParticleBlockHitPacket(BlockPos pos, int face, net.minecraft.world.phys.Vec3 hitVec, CompoundTag extra) {
        this.pos = pos;
        this.face = face;
        this.hitX = hitVec.x;
        this.hitY = hitVec.y;
        this.hitZ = hitVec.z;
        this.extra = extra==null ? new CompoundTag():extra;
    }

    public ServerboundParticleBlockHitPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.face = buf.readInt();
        this.hitX = buf.readDouble();
        this.hitY = buf.readDouble();
        this.hitZ = buf.readDouble();
        this.extra = buf.readNbt(); // может вернуть null в некоторых версиях -> проверяй
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(face);
        buf.writeDouble(hitX);
        buf.writeDouble(hitY);
        buf.writeDouble(hitZ);
        buf.writeNbt(extra);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            LOGGER.info("ServerboundParticleBlockHitPacket start");

            ServerPlayer sender = ctx.getSender();
            if (sender==null) return;

            Level level = sender.level();
            BlockState state = level.getBlockState(pos);

            // Извлекаем coreId/ownerId из extra (по соглашению ключи "coreId" и "ownerId")
            int coreId = extra!=null && extra.contains("coreId") ? extra.getInt("coreId"):-1;

            LOGGER.info("Block hit at {} state {} coreId {}", pos, state, coreId);

            if (state.getBlock()==Blocks.TNT) {
                if (coreId==CoreTypes.FIRE.getId()) {
                    level.removeBlock(pos, false);
                    PrimedTnt primed = new PrimedTnt(
                        level,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        sender
                    );
                    level.addFreshEntity(primed);
                    LOGGER.info("Ignited TNT at {}", pos);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
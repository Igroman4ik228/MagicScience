package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.network.IClientPacket;
import com.magicscience.magicsciencemod.network.IPacket;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.magicscience.magicsciencemod.network.magicparticles.*;
import com.magicscience.magicsciencemod.network.mana.ClientSyncManaPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.magicscience.magicsciencemod.util.ResourceLocationHelper.prefix;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        prefix("main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    private static int packetId = 0;

    public static void register() {
        // Server
        registerServer(
            ServerParticleBlockHitPacket.class,
            ServerParticleBlockHitPacket::encode,
            ServerParticleBlockHitPacket::new
        );
        registerServer(
            ServerCastParticlePacket.class,
            ServerCastParticlePacket::encode,
            ServerCastParticlePacket::new
        );
        registerServer(
            ServerParticleBlockHitPacket.class,
            ServerParticleBlockHitPacket::encode,
            ServerParticleBlockHitPacket::new
        );
        registerServer(
            ServerParticleEntityHitPacket.class,
            ServerParticleEntityHitPacket::encode,
            ServerParticleEntityHitPacket::new
        );

        // Client
        registerClient(
            ClientSpawnParticlePacket.class,
            ClientSpawnParticlePacket::encode,
            ClientSpawnParticlePacket::new
        );
        registerClient(
            ClientRemoveParticlePacket.class,
            ClientRemoveParticlePacket::encode,
            ClientRemoveParticlePacket::new
        );
        registerClient(
            ClientSyncManaPacket.class,
            ClientSyncManaPacket::encode,
            ClientSyncManaPacket::new
        );
    }

    private static <T extends IServerPacket> void registerServer(
        Class<T> clazz,
        BiConsumer<T, FriendlyByteBuf> encoder,
        Function<FriendlyByteBuf, T> decoder
    ) {
        CHANNEL.registerMessage(
            packetId++,
            clazz,
            encoder,
            decoder,
            createServerHandler(),
            Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }

    private static <T extends IClientPacket> void registerClient(
        Class<T> clazz,
        BiConsumer<T, FriendlyByteBuf> encoder,
        Function<FriendlyByteBuf, T> decoder
    ) {
        CHANNEL.registerMessage(
            packetId++,
            clazz,
            encoder,
            decoder,
            createClientHandler(),
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    private static <T extends IPacket> void handleCommon(
        T msg,
        Supplier<NetworkEvent.Context> ctxSupplier,
        Runnable mainTask
    ) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        if (!msg.prepare()) {
            ctx.setPacketHandled(true);
            return;
        }

        ctx.enqueueWork(mainTask);

        ctx.setPacketHandled(true);
    }


    private static <T extends IServerPacket> BiConsumer<T, Supplier<NetworkEvent.Context>> createServerHandler() {
        return (msg, ctxSupplier) -> {
            NetworkEvent.Context ctx = ctxSupplier.get();
            ServerPlayer player = ctx.getSender();
            if (player==null) {
                ctx.setPacketHandled(true);
                return;
            }

            handleCommon(msg, ctxSupplier, () -> msg.handle(player));

            ctx.setPacketHandled(true);
        };
    }

    private static <T extends IClientPacket> BiConsumer<T, Supplier<NetworkEvent.Context>> createClientHandler() {
        return (msg, ctxSupplier) -> handleCommon(msg, ctxSupplier, msg::handle);
    }
}
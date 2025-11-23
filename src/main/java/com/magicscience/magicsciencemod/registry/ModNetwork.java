package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.network.IClientPacket;
import com.magicscience.magicsciencemod.network.IServerPacket;
import com.magicscience.magicsciencemod.network.magicparticles.*;
import com.magicscience.magicsciencemod.network.mana.ClientSyncManaPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

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
        registerClient(
            ClientParticleObserverReassignPacket.class,
            ClientParticleObserverReassignPacket::encode,
            ClientParticleObserverReassignPacket::new
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

    @NotNull
    private static <T extends IServerPacket> BiConsumer<T, Supplier<NetworkEvent.Context>> createServerHandler() {
        return (msg, ctxSupplier) -> {
            NetworkEvent.Context ctx = ctxSupplier.get();

            if (!msg.prepare()) {
                ctx.setPacketHandled(true);
                return;
            }

            ServerPlayer player = ctx.getSender();
            if (player==null) {
                ctx.setPacketHandled(true);
                return;
            }

            ctx.enqueueWork(() -> msg.handle(player));

            ctx.setPacketHandled(true);
        };
    }

    @NotNull
    private static <T extends IClientPacket> BiConsumer<T, Supplier<NetworkEvent.Context>> createClientHandler() {
        return (msg, ctxSupplier) -> {
            NetworkEvent.Context ctx = ctxSupplier.get();

            if (!msg.prepare()) {
                ctx.setPacketHandled(true);
                return;
            }

            LocalPlayer player = Minecraft.getInstance().player;
            if (player==null) {
                ctx.setPacketHandled(true);
                return;
            }

            ctx.enqueueWork(() -> msg.handle(player));
            
            ctx.setPacketHandled(true);
        };
    }
}
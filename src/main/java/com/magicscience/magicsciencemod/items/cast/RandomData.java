package com.magicscience.magicsciencemod.items.cast;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public record RandomData(long seed) {
    public static @NotNull RandomData decode(@NotNull FriendlyByteBuf buf) {
        return new RandomData(buf.readLong());
    }

    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeLong(seed);
    }
}

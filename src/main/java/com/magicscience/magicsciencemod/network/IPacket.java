package com.magicscience.magicsciencemod.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IPacket {
    /**
     * Optional preparation / validation step executed in the network thread before the packet is handled.
     *
     * <p>Use this to validate incoming data, to reject malformed packets early, or to perform any inexpensive
     * transformations that must happen off the main thread. If this method returns {@code false}, the packet's
     * {@code handle} method will not be called.</p>
     *
     * <p>Default implementation returns {@code true} (no validation).</p>
     *
     * @return {@code true} to continue processing and enqueue handling on the main thread; {@code false} to cancel.
     */
    default boolean prepare() {
        return true;
    }

    void encode(FriendlyByteBuf buf);
}

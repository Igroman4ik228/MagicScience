package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record CastData(
    @NotNull SpellData spellData,
    @NotNull RandomData randomData,
    @NotNull Vec3 eyePosition,
    @NotNull Vec3 lookAngel,
    @NotNull Vec3 centerPosition
) {
    public static @NotNull CastData decode(@NotNull FriendlyByteBuf buf) {
        SpellData spellData = SpellData.decode(buf);
        RandomData randomData = RandomData.decode(buf);

        Vec3 eyePos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3 lookAngel = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3 centerPos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());

        return new CastData(spellData, randomData, eyePos, lookAngel, centerPos);
    }

    public void encode(@NotNull FriendlyByteBuf buf) {
        spellData.encode(buf);
        randomData.encode(buf);

        buf.writeDouble(eyePosition.x);
        buf.writeDouble(eyePosition.y);
        buf.writeDouble(eyePosition.z);

        buf.writeDouble(lookAngel.x);
        buf.writeDouble(lookAngel.y);
        buf.writeDouble(lookAngel.z);

        buf.writeDouble(centerPosition.x);
        buf.writeDouble(centerPosition.y);
        buf.writeDouble(centerPosition.z);
    }
}

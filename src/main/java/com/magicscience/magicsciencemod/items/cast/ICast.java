package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.spell.Spell;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public interface ICast {
    void castClient(LocalPlayer player);

    void castServer(@NotNull ServerPlayer player);

    Spell getSpell(Player player);

    void setSpell(Spell newSpell);
}

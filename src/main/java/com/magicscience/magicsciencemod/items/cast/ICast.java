package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.spell.Spell;
import net.minecraft.world.entity.player.Player;

public interface ICast {
    void cast(Player player, int manaCost);

    Spell getSpell();

    void setSpell(Spell newSpell);
}

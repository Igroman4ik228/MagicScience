package com.magicscience.magicsciencemod.items.cast;

import com.magicscience.magicsciencemod.aspects.spell.Spell;
import net.minecraft.world.entity.player.Player;

public interface ICast {
    public Spell getSpell();
    public void setSpell(Spell newSpell);

    public void cast(Player player, int manaCost);
}

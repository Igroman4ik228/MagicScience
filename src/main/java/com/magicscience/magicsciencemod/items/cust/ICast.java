package com.magicscience.magicsciencemod.items.cust;

import com.magicscience.magicsciencemod.aspects.Spell;

public interface ICast {
    public Spell getSpell();

    public void setSpell(Spell newSpell);
}

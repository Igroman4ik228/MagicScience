package com.magicscience.magicsciencemod.items.cust;

import com.magicscience.magicsciencemod.aspects.Spell;

public interface ICast {
    public void setSpell(Spell newSpell);
    public Spell getSpell();
}

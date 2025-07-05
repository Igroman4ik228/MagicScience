package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.SpellData;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.cores.FireCore;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;

public class CoreHandler {

    public static IMagicCore handle(SpellData spellData) {
        int coreId = spellData.coreId();
        CoreTypes coreType = CoreTypes.fromId(coreId);

        return switch (coreType) {
            case FIRE -> new FireCore();
        };
    }
}
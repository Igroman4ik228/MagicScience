package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;

public class MagicCoreFactory extends BaseMagicFactory<IMagicCore, CoreTypes> {
    public MagicCoreFactory() {
        super(CoreTypes.class);
    }
}

package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.cores.CoreType;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;

public class MagicCoreFactory extends BaseMagicFactory<IMagicCore, CoreType> {
    public MagicCoreFactory() {
        super(CoreType.class);
    }
}

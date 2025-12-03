package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureType;

public class MagicStructureFactory extends BaseMagicFactory<IMagicStructure, StructureType> {
    public MagicStructureFactory() {
        super(StructureType.class);
    }
}

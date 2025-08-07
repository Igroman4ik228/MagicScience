package com.magicscience.magicsciencemod.aspects.factories;

import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;

public class MagicStructureFactory extends BaseMagicFactory<IMagicStructure, StructureTypes> {
    public MagicStructureFactory() {
        super(StructureTypes.class);
    }
}

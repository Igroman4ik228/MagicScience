package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import net.minecraft.world.phys.Vec3;

public interface IMagicStructure extends IMagicAspect {
    int getCountParticles();
    IMagicStructure cloneWithArguments(Object... args);
    int getStack();
    int getSize();
    Vec3 calculateStartParticlePosition(Vec3 basePosition);
}

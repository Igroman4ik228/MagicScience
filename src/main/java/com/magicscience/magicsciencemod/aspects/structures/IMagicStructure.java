package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface IMagicStructure extends IMagicAspect {
    int getCountParticles();
    IMagicStructure cloneWithArguments(Object... args);
    int getStack();
    int getSize();
    List<Vec3> calculateStartParticlePositions(Vec3 basePosition);
}

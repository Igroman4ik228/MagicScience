package com.magicscience.magicsciencemod.aspects.structures;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface IMagicStructure extends IMagicAspect {
    int getCountParticles();

    @NotNull
    IMagicStructure cloneWithArguments(Object... args);

    int getStack();

    int getSize();

    @NotNull
    Vec3 calculateStartParticlePosition(@NotNull Vec3 basePosition);
}

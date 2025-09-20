package com.magicscience.magicsciencemod.aspects.structures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface IDynamicMagicStructure {
    Vec3 calculateStartParticleVectors(Vec3 startPosition, Player player);
}

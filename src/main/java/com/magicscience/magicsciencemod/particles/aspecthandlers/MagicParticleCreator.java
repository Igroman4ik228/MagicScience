package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.particles.MagicParticleOptions;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class MagicParticleCreator {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull SpellData spellData;
    private final @NotNull Vec3 position;
    private final @NotNull Vec3 direction;

    public MagicParticleCreator(@NotNull SpellData spellData, @NotNull Vec3 position, @NotNull Vec3 direction) {
        this.spellData = spellData;
        this.position = position;
        this.direction = direction;
    }

    public void create() {
        var spell = SpellConverter.toSpell(spellData);

        var level = Minecraft.getInstance().level;
        if (level == null) return;

        var particlePositions = spell.getStructure().calculateStartParticlePositions(position);

        for (var pos : particlePositions) {
            level.addParticle(
                new MagicParticleOptions(
                    spellData.ownerId(),
                    spellData.coreId(),
                    spellData.coreStack(),
                    spellData.attributeIds(),
                    spellData.attributeStack(),
                    spellData.structureId(),
                    spellData.structureStack(),
                    spellData.particleSpeed(),
                    spellData.particleLifeTime()
                ),
                pos.x, pos.y, pos.z,
                direction.x, direction.y, direction.z
            );
        }
    }
}

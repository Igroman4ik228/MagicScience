package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.particles.MagicParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MagicParticleCreator {
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

        int particleCount = spell.getMagicCore().getParticleCount();
        float particleSize = spell.getMagicCore().getSize();

        // ToDo: rename and refactor
        var positions = calculateStructure(position, particleCount, 0.3, particleSize);

        for (var pos : positions) {
            level.addParticle(
                new MagicParticleOptions(
                    spellData.ownerId(),
                    spellData.coreId(),
                    spellData.attributeIds(),
                    spellData.structureId(),
                    spellData.particleSpeed(),
                    spellData.particleLifeTime()
                ),
                pos.x, pos.y, pos.z,
                direction.x, direction.y, direction.z
            );
        }
    }

    private List<Vec3> calculateStructure(Vec3 basePosition, int count, double spread, double size) {
        List<Vec3> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double offsetX = (Math.random() - size) * spread;
            double offsetY = (Math.random() - size) * spread;
            double offsetZ = (Math.random() - size) * spread;
            result.add(basePosition.add(offsetX, offsetY, offsetZ));
        }
        return result;
    }
}

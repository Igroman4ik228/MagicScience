package com.magicscience.magicsciencemod.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.particles.MagicParticleOptions;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

        int particleCount = spell.getMagicCore().getParticleCount();
        float particleSize = spell.getMagicCore().getSize();

        var particlePositions = calculateParticlePositions(
            position,
            particleCount,
            0.3,
            particleSize
        );

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

    private List<Vec3> calculateParticlePositions(Vec3 basePosition, int count, double spread, double size) {
        List<Vec3> result = new ArrayList<>(count);
        double radius = spread * size;
        var rnd = ThreadLocalRandom.current();

        for (int i = 0; i < count; i++) {
            double u = rnd.nextDouble();
            double r = radius * Math.cbrt(u);

            // Случайные уголовые координаты
            double theta = Math.acos(2 * rnd.nextDouble() - 1);    // полярный угол [0, π]
            double phi = 2 * Math.PI * rnd.nextDouble();         // азимут [0, 2π)

            // Перевод в декартовы координаты
            double x = r * Math.sin(theta) * Math.cos(phi);
            double y = r * Math.sin(theta) * Math.sin(phi);
            double z = r * Math.cos(theta);

            result.add(basePosition.add(x, y, z));
        }
        return result;
    }
}

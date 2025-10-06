package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.SpreadingAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureContext;
import com.magicscience.magicsciencemod.aspects.structures.dynamic.DynamicStructureContext;
import com.magicscience.magicsciencemod.aspects.structures.dynamic.IDynamicMagicStructure;
import com.magicscience.magicsciencemod.client.particles.MagicParticleOptions;
import com.magicscience.magicsciencemod.items.cast.CastData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MagicParticleCreator {
    private final @NotNull CastData castData;
    private final @NotNull Random random;
    private final @NotNull MagicParticleOptions particleOptions;

    public MagicParticleCreator(@NotNull CastData castData) {
        this.castData = castData;
        this.random = new Random(castData.randomData().seed());

        SpellData spellData = castData.spellData();
        this.particleOptions = new MagicParticleOptions(
            spellData.ownerUUID(),
            spellData.coreId(),
            spellData.coreStack(),
            spellData.attributeIds(),
            spellData.attributeStack(),
            spellData.structureId(),
            spellData.structureStack(),
            spellData.particleSpeed(),
            spellData.particleLifeTime()
        );
    }

    @OnlyIn(Dist.CLIENT)
    public void create() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level==null) return;

        var spell = SpellConverter.toSpell(castData.spellData());

        boolean isSpreading = containsSpreadingAttribute(spell.getMagicAttributes());

        Vec3 baseVelocity = getBaseVelocity(castData.lookAngel(), isSpreading);

        IMagicStructure structure = spell.getStructure();
        if (structure==null) {
            spawnSingleParticle(level, castData.eyePosition(), baseVelocity);
        } else {
            spawnStructuredParticles(level, structure, isSpreading, baseVelocity);
        }
    }

    @NotNull
    private Vec3 getBaseVelocity(@NotNull Vec3 direction, boolean isSpreading) {
        var directionY = direction.y;
        if (isSpreading)
            directionY = 0;

        return new Vec3(
            direction.x,
            directionY,
            direction.z
        );
    }

    private void spawnSingleParticle(@NotNull ClientLevel level, @NotNull Vec3 pos, @NotNull Vec3 velocity) {
        level.addParticle(
            particleOptions,
            pos.x, pos.y, pos.z,
            velocity.x, velocity.y, velocity.z
        );
    }

    private void spawnStructuredParticles(
        @NotNull ClientLevel level,
        @NotNull IMagicStructure structure,
        boolean isSpreading,
        @NotNull Vec3 baseVelocity
    ) {
        for (int i = 0; i < structure.getCountParticles(); i++) {
            Vec3 startPos = structure.calculateStartParticlePosition(
                new StructureContext(
                    random,
                    castData.eyePosition(),
                    castData.eyePosition(),
                    castData.lookAngel()
                )
            );

            // For dynamic structure
            Vec3 velocityOffset = calculateOffset(structure, startPos);

            Vec3 velocity = baseVelocity.add(velocityOffset);

            double pY = startPos.y;
            if (isSpreading) {
                pY = castData.eyePosition().y;
            }

            level.addParticle(
                particleOptions,
                startPos.x, pY, startPos.z,
                velocity.x, velocity.y, velocity.z
            );
        }
    }

    private Vec3 calculateOffset(@NotNull IMagicStructure structure, @NotNull Vec3 startPos) {
        if (structure instanceof IDynamicMagicStructure dynamicStructure) {
            return dynamicStructure.calculateStartParticleVectors(
                new DynamicStructureContext(
                    random,
                    startPos,
                    castData.eyePosition(),
                    castData.lookAngel(),
                    castData.centerPosition()
                )
            );
        }

        return Vec3.ZERO;
    }

    private boolean containsSpreadingAttribute(@NotNull Iterable<IMagicAttribute> attributes) {
        for (IMagicAttribute attr : attributes) {
            if (attr instanceof SpreadingAttribute) {
                return true;
            }
        }
        return false;
    }
}

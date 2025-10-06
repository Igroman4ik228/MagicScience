package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.SpreadingAttribute;
import com.magicscience.magicsciencemod.aspects.spell.Spell;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class MagicParticleCreator {
    private final @NotNull CastData castData;
    private final @NotNull RandomSource random;
    private final @NotNull MagicParticleOptions particleOptions;
    private final @NotNull Spell spell;
    private final boolean hasSpreadingAttribute;

    public MagicParticleCreator(@NotNull CastData castData) {
        this.castData = castData;

        this.random = RandomSource.create(castData.randomData().seed());

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
        this.spell = SpellConverter.toSpell(spellData);
        this.hasSpreadingAttribute = containsSpreadingAttribute(
            spell.getMagicAttributes()
        );
    }

    @OnlyIn(Dist.CLIENT)
    public void create() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level==null)
            return;

        Vec3 direction = castData.lookAngel().scale(spell.getParticleSpeed());
        Vec3 baseVelocity = calculateBaseVelocity(direction);

        IMagicStructure structure = spell.getStructure();
        if (structure==null) {
            spawnSingleParticle(level, castData.eyePosition(), baseVelocity);
        } else {
            spawnStructuredParticles(level, structure, baseVelocity);
        }
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
        @NotNull Vec3 baseVelocity
    ) {
        StructureContext structureContext = new StructureContext(
            random,
            castData.eyePosition(),
            castData.eyePosition(),
            castData.lookAngel()
        );

        for (int i = 0; i < structure.getCountParticles(); i++) {
            Vec3 startPos = structure.calculateStartParticlePosition(structureContext);

            Vec3 velocity = calculateParticleVelocity(structure, startPos, baseVelocity);

            Vec3 pos = calculateSpawnPosition(startPos);
            level.addParticle(
                particleOptions,
                pos.x, pos.y, pos.z,
                velocity.x, velocity.y, velocity.z
            );
        }
    }

    @NotNull
    private Vec3 calculateBaseVelocity(@NotNull Vec3 direction) {
        return new Vec3(
            direction.x,
            hasSpreadingAttribute ? 0:direction.y,
            direction.z
        );
    }

    @NotNull
    private Vec3 calculateParticleVelocity(@NotNull IMagicStructure structure, @NotNull Vec3 startPos, @NotNull Vec3 baseVelocity) {
        // For dynamic structure
        Vec3 velocityOffset = calculateVelocityOffset(structure, startPos);
        
        return baseVelocity.add(velocityOffset);
    }

    private Vec3 calculateVelocityOffset(@NotNull IMagicStructure structure, @NotNull Vec3 startPos) {
        if (!(structure instanceof IDynamicMagicStructure dynamicStructure))
            return Vec3.ZERO;

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

    @NotNull
    private Vec3 calculateSpawnPosition(@NotNull Vec3 startPos) {
        return new Vec3(
            startPos.x,
            hasSpreadingAttribute ? castData.eyePosition().y:startPos.y,
            startPos.z
        );
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

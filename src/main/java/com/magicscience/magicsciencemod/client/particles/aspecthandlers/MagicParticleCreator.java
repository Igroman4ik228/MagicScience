package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.SpreadingAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.dynamic.IDynamicMagicStructure;
import com.magicscience.magicsciencemod.client.particles.MagicParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class MagicParticleCreator {
    private final @NotNull SpellData spellData;
    private final @NotNull Vec3 position;
    private final @NotNull Vec3 direction;
    private final @NotNull MagicParticleOptions particleOptions;

    public MagicParticleCreator(@NotNull SpellData spellData, @NotNull Vec3 position, @NotNull Vec3 direction) {
        this.spellData = spellData;
        this.position = position;
        this.direction = direction;

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
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player==null) return;
        ClientLevel level = mc.level;
        if (level==null) return;

        var spell = SpellConverter.toSpell(spellData);
        boolean isSpreading = containsSpreadingAttribute(spell.getMagicAttributes());
        Vec3 baseVelocity = getBaseVelocity(isSpreading);

        IMagicStructure structure = spell.getStructure();
        if (structure==null) {
            spawnSingleParticle(level, player, baseVelocity);
        } else {
            spawnStructuredParticles(level, player, structure, isSpreading, baseVelocity);
        }
    }

    private Vec3 getBaseVelocity(boolean isSpreading) {
        var directionY = direction.y;
        if (isSpreading)
            directionY = 0;

        return new Vec3(
            direction.x,
            directionY,
            direction.z
        );
    }

    private void spawnSingleParticle(@NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull Vec3 velocity) {
        Vec3 playerPos = player.position().add(0, 1, 0);
        level.addParticle(
            particleOptions,
            playerPos.x, playerPos.y, playerPos.z,
            velocity.x, velocity.y, velocity.z
        );
    }

    private void spawnStructuredParticles(
        @NotNull ClientLevel level,
        @NotNull LocalPlayer player,
        @NotNull IMagicStructure structure,
        boolean isSpreading,
        @NotNull Vec3 baseVelocity
    ) {
        for (int i = 0; i < structure.getCountParticles(); i++) {
            Vec3 startPos = structure.calculateStartParticlePosition(position);
            // For dynamic structure
            Vec3 offset = calculateOffset(structure, startPos, player);

            Vec3 velocity = baseVelocity.add(offset);

            double pY = startPos.y;
            if (isSpreading) {
                pY = player.position().y;
            }

            level.addParticle(
                particleOptions,
                startPos.x, pY, startPos.z,
                velocity.x, velocity.y, velocity.z
            );
        }
    }

    private Vec3 calculateOffset(@NotNull IMagicStructure structure, @NotNull Vec3 startPos, @NotNull LocalPlayer player) {
        if (structure instanceof IDynamicMagicStructure dynamicStructure) {
            return dynamicStructure.calculateStartParticleVectors(startPos, player);
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

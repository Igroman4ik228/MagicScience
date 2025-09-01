package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.SpreadingAttribute;
import com.magicscience.magicsciencemod.aspects.spell.SpellConverter;
import com.magicscience.magicsciencemod.aspects.spell.SpellData;
import com.magicscience.magicsciencemod.client.particles.MagicParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class MagicParticleCreator {
    private final @NotNull SpellData spellData;
    private final @NotNull Vec3 position;
    private final @NotNull Vec3 direction;
    private final @NotNull MagicParticleOptions pParticleData;

    public MagicParticleCreator(@NotNull SpellData spellData, @NotNull Vec3 position, @NotNull Vec3 direction) {
        this.spellData = spellData;
        this.position = position;
        this.direction = direction;

        this.pParticleData = new MagicParticleOptions(
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
        var mc = Minecraft.getInstance();

        var player = mc.player;
        if (player==null) return;
        var level = mc.level;
        if (level==null) return;

        var spell = SpellConverter.toSpell(spellData);
        var structure = spell.getStructure();
        if (structure==null) return;

        var attributes = spell.getMagicAttributes();
        boolean isSpreading = containsSpreadingAttribute(attributes);

        for (int i = 0; i < structure.getCountParticles(); i++) {
            var startPos = structure.calculateStartParticlePosition(position);

            double px = startPos.x;
            double py = isSpreading ? player.position().y:startPos.y;
            double pz = startPos.z;

            double vx = direction.x;
            double vy = isSpreading ? 0:direction.y;
            double vz = direction.z;

            level.addParticle(
                pParticleData,
                px, py, pz,
                vx, vy, vz
            );
        }
    }

    private boolean containsSpreadingAttribute(@NotNull Iterable<IMagicAttribute> attributes) {
        for (var attr : attributes) {
            if (attr instanceof SpreadingAttribute) {
                return true;
            }
        }
        return false;
    }


}

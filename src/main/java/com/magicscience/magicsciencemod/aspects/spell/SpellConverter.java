package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class SpellConverter {
    @NotNull
    public static SpellData toData(Spell spell) {
        Collection<IMagicAttribute> attributes = spell.getMagicAttributes();
        int[] attributeIds = attributes.stream()
            .mapToInt(AttributeTypes::getId)
            .toArray();

        return new SpellData(
            spell.getOwnerId(),
            CoreTypes.getId(spell.getMagicCore()),
            attributeIds,
            StructureTypes.getId(spell.getStructure()),
            spell.getParticleSpeed(),
            spell.getParticleLifeTime()
        );
    }

    @NotNull
    public static Spell toSpell(SpellData data) {
        Collection<IMagicAttribute> attributes = new ArrayList<>();
        for (int id : data.attributeIds()) {
            attributes.add(AttributeTypes.getInstance(id));
        }

        return new Spell(
            CoreTypes.getInstance(data.coreId()),
            attributes,
            StructureTypes.getInstance(data.structureId()),
            data.ownerId()
        );
    }
}

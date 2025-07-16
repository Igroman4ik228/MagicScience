package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpellConverter {
    @NotNull
    public static SpellData toData(Spell spell) {
        // ! I`m fuck tired convert OOP data to primitive for server code
        List<IMagicAttribute> attributes = spell.getMagicAttributes();
        int[] attributeIds = attributes.stream()
            .mapToInt(AttributeTypes::getId)
            .toArray();

        var attributeStack = new int[attributeIds.length];
        for (int i = 0; i != attributeStack.length; i++ ) {
            attributeStack[i] = attributes.get(i).getStack();
        }

        int structureStack = 0;
        if (spell.getStructure() != null) {
            structureStack = spell.getStructure().getStack();
        }

        return new SpellData(
            spell.getOwnerId(),
            CoreTypes.getId(spell.getMagicCore()),
            spell.getMagicCore().getStack(),
            attributeIds,
            attributeStack,
            StructureTypes.getId(spell.getStructure()),
            structureStack,
            spell.getParticleSpeed(),
            spell.getParticleLifeTime()
        );
    }

    @NotNull
    public static Spell toSpell(SpellData data) {
        List<IMagicAttribute> attributes = new ArrayList<>();

        for (int i = 0; i != data.attributeIds().length; i++) {
            attributes.add(AttributeTypes.getInstance(data.attributeIds()[i], data.attributeStack()[i]));
        }

        return new Spell(
            CoreTypes.getInstance(data.coreId(), data.coreStack()),
            attributes,
            StructureTypes.getInstance(data.structureId(), data.structureStack()),
            data.ownerId()
        );
    }
}

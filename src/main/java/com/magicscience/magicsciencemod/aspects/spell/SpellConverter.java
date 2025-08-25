package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypeHelper;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.factories.MagicAttributeFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicStructureFactory;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypeHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpellConverter {
    @NotNull
    public static SpellData toData(Spell spell) {
        List<IMagicAttribute> attributes = spell.getMagicAttributes();
        int[] attributeIds = attributes.stream()
            .mapToInt(AttributeTypeHelper::findId)
            .toArray();

        var attributeStack = new int[attributeIds.length];
        for (int i = 0; i!=attributeStack.length; i++) {
            attributeStack[i] = attributes.get(i).getStack();
        }

        int structureStack = 0;
        if (spell.getStructure()!=null) {
            structureStack = spell.getStructure().getStack();
        }

        return new SpellData(
            spell.getOwnerUUID(),
            CoreTypeHelper.findId(spell.getMagicCore()),
            spell.getMagicCore().getStack(),
            attributeIds,
            attributeStack,
            StructureTypeHelper.findId(spell.getStructure()),
            structureStack,
            spell.getParticleSpeed(),
            spell.getParticleLifeTime()
        );
    }

    @NotNull
    public static Spell toSpell(SpellData data) {
        List<IMagicAttribute> attributes = new ArrayList<>();

        var attributeFactory = new MagicAttributeFactory();
        var coreFactory = new MagicCoreFactory();
        var structureFactory = new MagicStructureFactory();

        for (int i = 0; i!=data.attributeIds().length; i++) {
            attributes.add(attributeFactory.createById(data.attributeIds()[i], data.attributeStack()[i]));
        }

        return new Spell(
            coreFactory.createById(data.coreId(), data.coreStack()),
            attributes,
            structureFactory.createById(data.structureId(), data.structureStack()),
            data.ownerUUID()
        );
    }
}

package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypeHelper;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypeHelper;
import com.magicscience.magicsciencemod.aspects.factories.MagicAttributeFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicCoreFactory;
import com.magicscience.magicsciencemod.aspects.factories.MagicStructureFactory;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypeHelper;
import com.magicscience.magicsciencemod.items.ScrollData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class SpellConverter {
    private static final MagicCoreFactory CORE_FACTORY = new MagicCoreFactory();
    private static final MagicAttributeFactory ATTRIBUTE_FACTORY = new MagicAttributeFactory();
    private static final MagicStructureFactory STRUCTURE_FACTORY = new MagicStructureFactory();

    @NotNull
    public static SpellData toData(@NotNull Spell spell) {
        var attributes = spell.getMagicAttributes();

        int[] attributeIds = attributes.stream()
            .mapToInt(AttributeTypeHelper::findId)
            .toArray();

        int[] attributeStacks = attributes.stream()
            .mapToInt(IMagicAttribute::getStack)
            .toArray();

        int structureId = spell.getStructure() != null ? spell.getStructure().getStack() : 0;

        return new SpellData(
            spell.getOwnerUUID(),
            CoreTypeHelper.findId(spell.getMagicCore()),
            spell.getMagicCore().getStack(),
            attributeIds,
            attributeStacks,
            StructureTypeHelper.findId(spell.getStructure()),
            structureId,
            spell.getParticleSpeed(),
            spell.getParticleLifeTime()
        );
    }

    @NotNull
    public static ScrollData toScroll(@NotNull SpellData spellData) {
        return new ScrollData(
            null,
            spellData.coreId(),
            spellData.coreStack(),
            spellData.attributeIds(),
            spellData.attributeStack(),
            spellData.structureId(),
            spellData.structureStack()
        );
    }

    @NotNull
    public static Spell toSpell(@NotNull SpellData data) {
        List<IMagicAttribute> attributes = createAttributes(data.attributeIds(), data.attributeStack());

        return new Spell(
            CORE_FACTORY.createById(data.coreId(), data.coreStack()),
            attributes,
            STRUCTURE_FACTORY.createById(data.structureId(), data.structureStack()),
            data.ownerUUID()
        );
    }

    @NotNull
    public static Spell toSpell(@NotNull ScrollData data, @NotNull UUID ownerUUID) {
        List<IMagicAttribute> attributes = createAttributes(data.attributeIds(), data.attributeStack());

        return new Spell(
            CORE_FACTORY.createById(data.coreId(), data.coreStack()),
            attributes,
            STRUCTURE_FACTORY.createById(data.structureId(), data.structureStack()),
            ownerUUID
        );
    }

    @NotNull
    private static List<IMagicAttribute> createAttributes(int[] ids, int[] stacks) {
        return IntStream.range(0, ids.length)
            .mapToObj(i -> ATTRIBUTE_FACTORY.createById(ids[i], stacks[i]))
            .toList();
    }
}

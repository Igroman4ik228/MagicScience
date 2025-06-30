package com.magicscience.magicsciencemod.aspects;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;

import java.util.Collection;

public class Spell implements IMagicAspect {
    private final IMagicCore magicCore;
    private final Collection<IMagicAttribute> magicAttribute;
    private final IMagicStructure magicStructure;
    private final int ownerId;
    private int particleSpeed;

    public Spell(
            IMagicCore magicCore,
            Collection<IMagicAttribute> magicAttribute,
            IMagicStructure magicStructure,
            int ownerId) {
        this.magicCore = magicCore;
        this.magicAttribute = magicAttribute;
        this.magicStructure = magicStructure;
        this.ownerId = ownerId;

        // ToDo: Pattern builder
        setParticleSpeed();
    }

    public Spell(IMagicCore magicCore, Collection<IMagicAttribute> magicAttribute, int ownerId) {
        this(magicCore, magicAttribute, null, ownerId);
    }

    public Spell(IMagicCore magicCore, int ownerId) {
        this(magicCore, null, null, ownerId);
    }

    @Override
    public int getManaCost() {
        int totalCost = magicCore.getManaCost();

        if (magicAttribute != null) {
            for (IMagicAttribute attribute : magicAttribute) {
                totalCost += attribute.getManaCost();
            }
        }

        if (magicStructure != null)
            totalCost += magicStructure.getManaCost();

        return totalCost;
    }

    public SpellData toData() {
        int structureId = StructureTypes.NONE.getCode();
        if (magicStructure != null)
            structureId = magicStructure.getCode();

        int[] attributeIds = new int[0];
        if (magicAttribute != null) {
            attributeIds = magicAttribute.stream()
                    .map(attr -> attr.getAttributeTypes().getCode())
                    .mapToInt(Integer::intValue)
                    .toArray();
        }

        return new SpellData(
                ownerId,
                magicCore.getCode(),
                attributeIds,
                structureId,
                particleSpeed);
    }

    public IMagicCore getMagicCore() {
        return magicCore;
    }

    public Collection<IMagicAttribute> getMagicAttributes() {
        return magicAttribute;
    }

    public IMagicStructure getStructure() {
        return magicStructure;
    }

    public void setParticleSpeed() {
        if (magicAttribute == null){
            particleSpeed = 0;
            return;
        }

        // ToDo: Calc with Math
        for (IMagicAttribute attribute : magicAttribute) {
            if (attribute.getAttributeTypes() == AttributeTypes.VECTOR){
                particleSpeed = 10;
            }
        }
    }

    public int getParticleSpeed() {
        return particleSpeed;
    }
}

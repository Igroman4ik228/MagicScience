package com.magicscience.magicsciencemod.aspects;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.registry.AspectsRegistry;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;

import java.util.Collection;

public class Spell implements IMagicAspect {
    private final IMagicCore magicCore;
    private final Collection<IMagicAttribute> magicAttributes;
    private final IMagicStructure magicStructure;
    private final int ownerId;
    private final int particleSpeed;
    private final int particleLifeTime;

    public Spell(
        IMagicCore magicCore,
        Collection<IMagicAttribute> magicAttributes,
        IMagicStructure magicStructure,
        int ownerId
    ) {
        this.magicCore = magicCore;
        this.magicAttributes = magicAttributes;
        this.magicStructure = magicStructure;
        this.ownerId = ownerId;
        // ToDo: Math and mb pattern builder
        this.particleSpeed = calculateParticleSpeed();
        this.particleLifeTime = calculateParticleLifeTime();
    }

    public Spell(IMagicCore magicCore, Collection<IMagicAttribute> magicAttributes, int ownerId) {
        this(magicCore, magicAttributes, null, ownerId);
    }

    public Spell(IMagicCore magicCore, int ownerId) {
        this(magicCore, null, null, ownerId);
    }

    public SpellData toData() {
        int structureId = StructureTypes.NONE.getId();
        if (magicStructure != null)
            structureId = AspectsRegistry.getStructureTypeId(magicStructure);

        int[] attributeIds = new int[0];
        if (magicAttributes != null) {
            attributeIds = magicAttributes.stream()
                .map(AspectsRegistry::getAttributeTypeId)
                .mapToInt(Integer::intValue)
                .toArray();
        }

        return new SpellData(
            ownerId,
            AspectsRegistry.getCoreTypeId(magicCore),
            attributeIds,
            structureId,
            particleSpeed,
            particleLifeTime
        );
    }

    @Override
    public int getManaCost() {
        int totalCost = magicCore.getManaCost();

        if (magicAttributes != null) {
            for (IMagicAttribute attribute : magicAttributes) {
                totalCost += attribute.getManaCost();
            }
        }

        if (magicStructure != null)
            totalCost += magicStructure.getManaCost();

        return totalCost;
    }

    public IMagicCore getMagicCore() {
        return magicCore;
    }

    public Collection<IMagicAttribute> getMagicAttributes() {
        return magicAttributes;
    }

    public IMagicStructure getStructure() {
        return magicStructure;
    }

    private int calculateParticleSpeed() {
        if (magicAttributes == null) return 0;

        // ToDo: Calc with Math
        for (IMagicAttribute attribute : magicAttributes) {

            if (AspectsRegistry.getAttributeTypeId(attribute) == AttributeTypes.VECTOR.getId()) {
                return 10;
            }
        }

        return 0;
    }

    private int calculateParticleLifeTime() {
        return magicCore.getParticleLifeTime();
    }

    public int getParticleSpeed() {
        return particleSpeed;
    }

    public int getParticleLifeTime() {
        return particleLifeTime;
    }
}

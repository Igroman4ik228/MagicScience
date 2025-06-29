package com.magicscience.magicsciencemod.aspects;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;

import java.util.Collection;

public class Spell implements IMagicAspect{

    private final IMagicCore magicCore;
    private final Collection<IMagicAttribute> magicAttribute;
    private final IMagicStructure magicStructure;
    private int particleSpeed;

    public Spell(IMagicCore magicCore, Collection<IMagicAttribute> magicAttribute, IMagicStructure magicStructure) {
        this.magicCore = magicCore;
        this.magicAttribute = magicAttribute;
        this.magicStructure = magicStructure;

        // ToDo: Pattern builder
        setParticleSpeed();
    }

    public Spell(IMagicCore magicCore, Collection<IMagicAttribute> magicAttribute) {
        this(magicCore, magicAttribute, null);
    }

    public Spell(IMagicCore magicCore) {
        this(magicCore, null, null);
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

    public IMagicCore getMagicCore(){
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

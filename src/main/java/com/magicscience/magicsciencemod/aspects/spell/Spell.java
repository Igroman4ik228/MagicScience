package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;
import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

// ToDo: pattern builder
public class Spell implements IMagicAspect {
    private final @NotNull IMagicCore magicCore;
    private final @NotNull Collection<IMagicAttribute> magicAttributes;
    private final @Nullable IMagicStructure magicStructure;
    private final int ownerId;
    private final int manaCost;
    private final int particleSpeed;
    private final int particleLifeTime;

    public Spell(
        @NotNull IMagicCore magicCore,
        @NotNull Collection<IMagicAttribute> magicAttributes,
        @Nullable IMagicStructure magicStructure,
        int ownerId
    ) {
        this.magicCore = magicCore;
        this.magicAttributes = magicAttributes;
        this.magicStructure = magicStructure;
        this.ownerId = ownerId;

        // ToDo: Math
        this.manaCost = calculateManaCost();
        this.particleSpeed = calculateParticleSpeed();
        this.particleLifeTime = calculateParticleLifeTime();
    }

    public Spell(
        @NotNull IMagicCore magicCore,
        @NotNull Collection<IMagicAttribute> magicAttributes,
        int ownerId
    ) {
        this(magicCore, magicAttributes, null, ownerId);
    }

    public Spell(
        @NotNull IMagicCore magicCore,
        int ownerId
    ) {
        this(magicCore, new ArrayList<>(), null, ownerId);
    }

    private int calculateManaCost() {
        int totalCost = magicCore.getManaCost();

        for (IMagicAttribute attribute : magicAttributes) {
            totalCost += attribute.getManaCost();
        }

        if (magicStructure != null)
            totalCost += magicStructure.getManaCost();

        return totalCost;
    }

    private int calculateParticleLifeTime() {
        return magicCore.getParticleLifeTime();
    }

    private int calculateParticleSpeed() {
        for (IMagicAttribute attribute : magicAttributes) {

            if (AttributeTypes.getId(attribute) == AttributeTypes.VECTOR.getId()) {
                return 2;
            }
        }

        return 0;
    }

    @NotNull
    public IMagicCore getMagicCore() {
        return magicCore;
    }

    @NotNull
    public Collection<IMagicAttribute> getMagicAttributes() {
        return magicAttributes;
    }

    @Nullable
    public IMagicStructure getStructure() {
        return magicStructure;
    }

    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    public int getParticleSpeed() {
        return particleSpeed;
    }

    public int getParticleLifeTime() {
        return particleLifeTime;
    }
}

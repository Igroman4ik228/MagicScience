package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.unique.IMagicParticleSpeed;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Spell implements ISpell {
    private final @NotNull IMagicCore magicCore;
    private final @NotNull List<IMagicAttribute> magicAttributes;
    private final @Nullable IMagicStructure magicStructure;
    private final @NotNull UUID ownerUUID;
    private final int manaCost;
    private final int particleSpeed;
    private final int particleLifeTime;

    public Spell(
        @NotNull IMagicCore magicCore,
        @NotNull List<IMagicAttribute> magicAttributes,
        @Nullable IMagicStructure magicStructure,
        @NotNull UUID ownerUUID
    ) {
        this.magicCore = magicCore;
        this.magicAttributes = magicAttributes;
        this.magicStructure = magicStructure;
        this.ownerUUID = ownerUUID;

        this.manaCost = calculateManaCost();
        this.particleSpeed = calculateParticleSpeed();
        this.particleLifeTime = calculateParticleLifeTime();
    }

    public Spell(
        @NotNull IMagicCore magicCore,
        @NotNull List<IMagicAttribute> magicAttributes,
        @NotNull UUID ownerUUID
    ) {
        this(magicCore, magicAttributes, null, ownerUUID);
    }

    public Spell(
        @NotNull IMagicCore magicCore,
        @NotNull UUID ownerUUID
    ) {
        this(magicCore, new ArrayList<>(), null, ownerUUID);
    }

    private int calculateParticleSpeed() {
        for (var attribute : getMagicAttributes()) {
            if (attribute instanceof IMagicParticleSpeed) {
                return ((IMagicParticleSpeed) attribute).getParticleSpeed();
            }
        }

        return 0;
    }

    public int calculateManaCost() {
        int totalCost = magicCore.getManaCost();

        for (IMagicAttribute attribute : magicAttributes) {
            totalCost += attribute.getManaCost();
        }

        if (magicStructure!=null)
            totalCost += magicStructure.getManaCost();

        return totalCost;
    }

    private int calculateParticleLifeTime() {
        return magicCore.getParticleLifeTime();
    }

    @NotNull
    public IMagicCore getMagicCore() {
        return magicCore;
    }

    @NotNull
    public List<IMagicAttribute> getMagicAttributes() {
        return magicAttributes;
    }

    @Nullable
    public IMagicStructure getStructure() {
        return magicStructure;
    }

    @NotNull
    public UUID getOwnerUUID() {
        return ownerUUID;
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

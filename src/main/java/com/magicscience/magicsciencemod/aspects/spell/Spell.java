package com.magicscience.magicsciencemod.aspects.spell;

import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.unique.IMagicParticleSpeed;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Spell implements ISpell {
    private final @NotNull IMagicCore magicCore;
    private final @NotNull Set<IMagicAttribute> magicAttributes;
    private final @Nullable IMagicStructure magicStructure;
    private final @NotNull UUID ownerUUID;
    private final int manaCost;
    private final int particleSpeed;
    private final int particleLifeTime;

    private Spell(
        @NotNull IMagicCore magicCore,
        @NotNull Set<IMagicAttribute> magicAttributes,
        @Nullable IMagicStructure magicStructure,
        @NotNull UUID ownerUUID,
        int manaCost,
        int particleSpeed,
        int particleLifeTime
    ) {
        this.magicCore = magicCore;
        this.magicAttributes = magicAttributes;
        this.magicStructure = magicStructure;
        this.ownerUUID = ownerUUID;
        this.manaCost = manaCost;
        this.particleSpeed = particleSpeed;
        this.particleLifeTime = particleLifeTime;
    }

    @NotNull
    public static Builder builder(@NotNull IMagicCore core, @NotNull UUID ownerUUID) {
        return new Builder(core, ownerUUID);
    }

    @NotNull
    public IMagicCore getMagicCore() {
        return magicCore;
    }

    @NotNull
    public Set<IMagicAttribute> getMagicAttributes() {
        return magicAttributes;
    }

    @Nullable
    public <T extends IMagicAttribute> T getMagicAttribute(Class<T> cls) {
        for (var attr : magicAttributes) {
            if (cls.isInstance(attr))
                return cls.cast(attr);
        }

        return null;
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

    public static class Builder {
        private final @NotNull Set<IMagicAttribute> attributes = new LinkedHashSet<>();
        private final @NotNull IMagicCore core;
        private final UUID ownerUUID;
        private @Nullable IMagicStructure structure = null;

        private Builder(@NotNull IMagicCore core, @NotNull UUID ownerUUID) {
            this.core = Objects.requireNonNull(core, "magicCore is required");
            this.ownerUUID = Objects.requireNonNull(ownerUUID, "ownerUUID is required");
        }

        private static int calculateManaCost(
            @NotNull IMagicCore core,
            @NotNull Set<IMagicAttribute> attributes,
            @Nullable IMagicStructure structure
        ) {
            int totalCost = core.getManaCost();

            for (IMagicAttribute attribute : attributes) {
                totalCost += attribute.getManaCost();
            }

            if (structure!=null)
                totalCost += structure.getManaCost();

            return totalCost;
        }

        private static int calculateParticleSpeed(@NotNull Set<IMagicAttribute> attributes) {
            for (var attribute : attributes) {
                if (attribute instanceof IMagicParticleSpeed particleSpeedAttribute) {
                    return particleSpeedAttribute.getParticleSpeed();
                }
            }

            return 0;
        }

        private static int calculateParticleLifeTime(@NotNull IMagicCore core) {
            return core.getParticleLifeTime();
        }

        public Builder magicAttributes(@NotNull Collection<IMagicAttribute> attrs) {
            for (IMagicAttribute attr : attrs) {
                boolean exists = attributes.stream().anyMatch(a -> a.getClass()==attr.getClass());
                if (!exists)
                    attributes.add(attr);
            }

            return this;
        }

        public Builder magicAttributes(@NotNull IMagicAttribute... attrs) {
            return magicAttributes(Arrays.asList(attrs));
        }

        public Builder magicStructure(@NotNull IMagicStructure structure) {
            this.structure = structure;
            return this;
        }

        @NotNull
        public Spell build() {
            int manaCost = calculateManaCost(core, attributes, structure);
            int particleSpeed = calculateParticleSpeed(attributes);
            int particleLifeTime = calculateParticleLifeTime(core);

            return new Spell(
                core,
                attributes,
                structure,
                ownerUUID,
                manaCost,
                particleSpeed,
                particleLifeTime
            );
        }
    }
}

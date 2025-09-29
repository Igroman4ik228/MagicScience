package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IMagicParticleSpeed;
import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.attribute.VectorAttributeConfig;
import org.jetbrains.annotations.NotNull;

public class VectorAttribute extends BaseMagicAttribute implements IMagicParticleSpeed {
    private static final VectorAttributeConfig CONFIG = (VectorAttributeConfig) AttributeConfig.get(AttributeTypes.VECTOR);

    private final int particleSpeed;

    public VectorAttribute(@NotNull BaseAttributeData baseAttributeData, int stack, int particleSpeed) {
        super(baseAttributeData, stack);
        this.particleSpeed = particleSpeed;
    }

    public VectorAttribute() {
        this(CONFIG.toData(), 1, CONFIG.getParticleSpeed());
    }

    public VectorAttribute(int stack) {
        this(CONFIG.toData(), stack, CONFIG.getParticleSpeed());
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new VectorAttribute((int) args[0]);
    }

    @Override
    public int getParticleSpeed() {
        int speed = particleSpeed * getStack();
        if (speed <= 1) {
            return 1;
        }

        return speed / 2;
    }
}

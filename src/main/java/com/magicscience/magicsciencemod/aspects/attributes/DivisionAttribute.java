package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.attribute.DivisionAttributeConfig;
import org.jetbrains.annotations.NotNull;

public class DivisionAttribute extends BaseMagicAttribute {
    private static final DivisionAttributeConfig CONFIG = (DivisionAttributeConfig) AttributeConfig.get(AttributeTypes.DIVISION);

    private final int multiplier;

    public DivisionAttribute(@NotNull BaseAttributeData baseAttributeData, int stack, int multiplier) {
        super(baseAttributeData, stack);
        this.multiplier = multiplier;
    }

    public DivisionAttribute() {
        this(CONFIG.toData(), 1, CONFIG.getMultiplier());
    }

    public DivisionAttribute(int stack) {
        this(CONFIG.toData(), stack, CONFIG.getMultiplier());
    }

    public float getMultiplier() {
        return multiplier * this.getStack();
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new DivisionAttribute((int) args[0]);
    }
}

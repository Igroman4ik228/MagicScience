package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.attribute.VectorAttributeConfig;
import org.jetbrains.annotations.NotNull;

public class SpreadingAttribute extends BaseMagicAttribute {
    private static final VectorAttributeConfig CONFIG = (VectorAttributeConfig) AttributeConfig.get(AttributeTypes.VECTOR);

    public SpreadingAttribute(@NotNull BaseAttributeData baseAttributeData, int stack) {
        super(baseAttributeData, stack);
    }

    public SpreadingAttribute() {
        this(CONFIG.toData(), 1);
    }

    public SpreadingAttribute(int stack) {
        this(CONFIG.toData(), stack);
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new SpreadingAttribute((int) args[0]);
    }
}

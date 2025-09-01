package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.attribute.SubsidenceAttributeConfig;
import org.jetbrains.annotations.NotNull;

public class SubsidenceAttribute extends BaseMagicAttribute {
    private static final SubsidenceAttributeConfig CONFIG = (SubsidenceAttributeConfig) AttributeConfig.get(AttributeTypes.SUBSIDENCE);

    private final float gravity;

    public SubsidenceAttribute(@NotNull BaseAttributeData baseAttributeData, int stack, float gravity) {
        super(baseAttributeData, stack);
        this.gravity = gravity;
    }

    public SubsidenceAttribute() {
        this(CONFIG.toData(), 1, CONFIG.getGravity());
    }

    public SubsidenceAttribute(int stack) {
        this(CONFIG.toData(), stack, CONFIG.getGravity());
    }

    public float getGravity() {
        return gravity * this.getStack();
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new SubsidenceAttribute((int) args[0]);
    }
}

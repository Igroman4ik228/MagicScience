package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.attribute.GravityAttributeConfig;
import org.jetbrains.annotations.NotNull;

public class GravityAttribute extends BaseMagicAttribute {
    private static final GravityAttributeConfig CONFIG = (GravityAttributeConfig) AttributeConfig.get(AttributeType.GRAVITY);

    private final float gravity;

    public GravityAttribute(@NotNull BaseAttributeData baseAttributeData, int stack, float gravity) {
        super(baseAttributeData, stack);
        this.gravity = gravity;
    }

    public GravityAttribute() {
        this(CONFIG.toData(), 1, CONFIG.getGravity());
    }

    public GravityAttribute(int stack) {
        this(CONFIG.toData(), stack, CONFIG.getGravity());
    }

    public float getGravity() {
        return gravity * this.getStack();
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new GravityAttribute((int) args[0]);
    }
}

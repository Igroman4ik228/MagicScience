package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;

public enum AttributeTypes {
    NONE(0),
    VECTOR(1),
    SELF_SPECTRE(2);

    private final int code;

    AttributeTypes(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static AttributeTypes fromCode(int code) {
        for (AttributeTypes type : AttributeTypes.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}

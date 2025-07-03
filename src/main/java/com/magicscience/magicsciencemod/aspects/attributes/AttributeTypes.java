package com.magicscience.magicsciencemod.aspects.attributes;

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
}

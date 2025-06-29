package com.magicscience.magicsciencemod.aspects.attributes;

public enum AttributeTypes {
    VECTOR(0);

    private final int code;

    AttributeTypes(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}

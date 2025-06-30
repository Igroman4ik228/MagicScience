package com.magicscience.magicsciencemod.aspects.structures;

public enum StructureTypes {
    NONE(0),
    CLOT(1);

    private final int code;

    StructureTypes(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}

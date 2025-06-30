package com.magicscience.magicsciencemod.aspects.cores;

public enum CoreTypes {
    NONE(0),
    FIRE(1);

    private final int code;

    CoreTypes(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}

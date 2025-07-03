package com.magicscience.magicsciencemod.aspects.cores;

public enum CoreTypes {
    FIRE(1);

    private final int code;

    CoreTypes(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static CoreTypes fromCode(int code) {
        for (CoreTypes type : CoreTypes.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}

package com.magicscience.magicsciencemod.aspects.cores;

public enum CoreTypes {
    FIRE(1);

    private final int id;

    CoreTypes(int id) {
        this.id = id;
    }

    public static CoreTypes fromId(int id) {
        for (CoreTypes type : CoreTypes.values()) {
            if (type.getId() == id) return type;
        }
        throw new IllegalArgumentException("Unknown id: " + id);
    }

    public int getId() {
        return id;
    }
}

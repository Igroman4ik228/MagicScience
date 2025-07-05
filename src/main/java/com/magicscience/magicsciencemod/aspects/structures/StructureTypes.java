package com.magicscience.magicsciencemod.aspects.structures;

public enum StructureTypes {
    NONE(0),
    CLOT(1);

    private final int id;

    StructureTypes(int id) {
        this.id = id;
    }

    public static StructureTypes fromId(int id) {
        for (StructureTypes type : StructureTypes.values()) {
            if (type.getId() == id) return type;
        }
        throw new IllegalArgumentException("Unknown id: " + id);
    }

    public int getId() {
        return id;
    }
}

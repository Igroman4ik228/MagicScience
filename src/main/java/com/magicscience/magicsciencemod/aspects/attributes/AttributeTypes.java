package com.magicscience.magicsciencemod.aspects.attributes;

public enum AttributeTypes {
    // ToDO:?
    // VECTOR(1, VectorAttribute::new),
    // SELF_SPECTRE(2, SelfSpectreAttribute::new);
    NONE(0),
    VECTOR(1),
    SELF_SPECTRE(2);

    private final int id;

    AttributeTypes(int id) {
        this.id = id;
    }

    public static AttributeTypes fromId(int id) {
        for (AttributeTypes type : AttributeTypes.values()) {
            if (type.getId() == id) return type;
        }
        throw new IllegalArgumentException("Unknown id: " + id);
    }

    public int getId() {
        return id;
    }
}

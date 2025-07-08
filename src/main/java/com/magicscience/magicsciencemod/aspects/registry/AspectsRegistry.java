package com.magicscience.magicsciencemod.aspects.registry;

import com.magicscience.magicsciencemod.aspects.attributes.AttributeTypes;
import com.magicscience.magicsciencemod.aspects.attributes.IMagicAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.SelfSpectreAttribute;
import com.magicscience.magicsciencemod.aspects.attributes.VectorAttribute;
import com.magicscience.magicsciencemod.aspects.cores.CoreTypes;
import com.magicscience.magicsciencemod.aspects.cores.FireCore;
import com.magicscience.magicsciencemod.aspects.cores.IMagicCore;
import com.magicscience.magicsciencemod.aspects.structures.ClotStructure;
import com.magicscience.magicsciencemod.aspects.structures.IMagicStructure;
import com.magicscience.magicsciencemod.aspects.structures.StructureTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AspectsRegistry {
    private static final GenericRegistry<IMagicAttribute> ATTRIBUTE_REGISTRY =
        new GenericRegistry<>("Атрибут");
    private static final GenericRegistry<@NotNull IMagicCore> CORE_REGISTRY =
        new GenericRegistry<>("Ядро");
    private static final GenericRegistry<IMagicStructure> STRUCTURE_REGISTRY =
        new GenericRegistry<>("Структура");

    // статический блок инициализации
    static {
        registerAttribute(AttributeTypes.NONE.getId(), null);
        registerAttribute(AttributeTypes.VECTOR.getId(), new VectorAttribute());
        registerAttribute(AttributeTypes.SELF_SPECTRE.getId(), new SelfSpectreAttribute());

        registerCore(CoreTypes.FIRE.getId(), new FireCore());

        registerStructure(StructureTypes.NONE.getId(), null);
        registerStructure(StructureTypes.CLOT.getId(), new ClotStructure());
    }

    private AspectsRegistry() {
    }

    // === ATTRIBUTE ===
    public static void registerAttribute(int typeId, IMagicAttribute instance) {
        ATTRIBUTE_REGISTRY.register(typeId, instance);
    }

    public static IMagicAttribute getAttribute(int typeId) {
        return ATTRIBUTE_REGISTRY.get(typeId);
    }

    public static int getAttributeTypeId(IMagicAttribute instance) {
        return ATTRIBUTE_REGISTRY.getTypeId(instance);
    }

    // === CORE ===
    public static void registerCore(int typeId, @NotNull IMagicCore instance) {
        CORE_REGISTRY.register(typeId, instance);
    }

    public static IMagicCore getCore(int typeId) {
        return CORE_REGISTRY.get(typeId);
    }

    public static int getCoreTypeId(IMagicCore instance) {
        return CORE_REGISTRY.getTypeId(instance);
    }

    // === STRUCTURE ===
    public static void registerStructure(int typeId, IMagicStructure instance) {
        STRUCTURE_REGISTRY.register(typeId, instance);
    }

    public static IMagicStructure getStructure(int typeId) {
        return STRUCTURE_REGISTRY.get(typeId);
    }

    public static int getStructureTypeId(IMagicStructure instance) {
        return STRUCTURE_REGISTRY.getTypeId(instance);
    }

    // (опционально) методы для перебора всех зарегистрированных
    public static Collection<IMagicAttribute> getAllAttributes() {
        return ATTRIBUTE_REGISTRY.getAll();
    }

    public static Collection<IMagicCore> getAllCores() {
        return CORE_REGISTRY.getAll();
    }

    public static Collection<IMagicStructure> getAllStructures() {
        return STRUCTURE_REGISTRY.getAll();
    }
}

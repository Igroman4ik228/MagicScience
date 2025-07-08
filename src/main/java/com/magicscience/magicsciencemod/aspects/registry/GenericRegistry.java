package com.magicscience.magicsciencemod.aspects.registry;

import com.magicscience.magicsciencemod.aspects.IMagicAspect;

import java.util.*;

public class GenericRegistry<T extends IMagicAspect> {
    private final Map<Integer, T> registry = new HashMap<>();
    private final String elementName;

    public GenericRegistry() {
        this("Элемент");
    }

    public GenericRegistry(String elementName) {
        this.elementName = elementName;
    }

    public void register(int typeId, T instance) {
        if (registry.containsKey(typeId)) {
            throw new IllegalArgumentException(
                elementName + " с typeId=" + typeId + " уже зарегистрирован"
            );
        }

        registry.put(typeId, instance);
    }

    public T get(int typeId) {
        if (!registry.containsKey(typeId)) {
            throw new IllegalArgumentException(
                "Нет зарегистрированного " + elementName + " с typeId=" + typeId
            );
        }

        return registry.get(typeId);
    }

    public int getTypeId(T instance) {
        return registry.entrySet()
            .stream()
            .filter(e -> Objects.equals(e.getValue(), instance))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Нет зарегистрированного typeId для " + elementName + ": " + instance
            ));
    }

    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(registry.values());
    }
}
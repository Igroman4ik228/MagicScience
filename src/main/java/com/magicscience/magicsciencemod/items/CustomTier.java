package com.magicscience.magicsciencemod.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class CustomTier implements Tier {

    @Override
    public int getUses() {
        return -1; // Количество использований (например, как у алмаза)
    }

    @Override
    public float getSpeed() {
        return 16.0F; // Скорость добычи (например, как у железного инструмента)
    }

    @Override
    public float getAttackDamageBonus() {
        return 50.0F; // Урон от удара (например, как у железного меча)
    }

    @Override
    public int getLevel() {
        return 3; // Уровень материала (например, как у алмаза)
    }

    @Override
    public int getEnchantmentValue() {
        return 10; // Степень зачарования (например, как у железа)
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}
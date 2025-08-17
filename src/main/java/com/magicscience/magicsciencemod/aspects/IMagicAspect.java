package com.magicscience.magicsciencemod.aspects;

public interface IMagicAspect {
    int getManaCost();

    default String getTranslationKey(){
        return "aspect.magicscience." + this.getClass().getSimpleName().toLowerCase();
    }
}

package com.magicscience.magicsciencemod.util;

import com.magicscience.magicsciencemod.MagicScienceMod;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MagicScienceMod.MOD_ID, path);
    }

    public static ModelResourceLocation modelResourceLocation(String path, String variant) {
        return new ModelResourceLocation(prefix(path), variant);
    }
}

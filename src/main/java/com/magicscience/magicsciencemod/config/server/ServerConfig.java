package com.magicscience.magicsciencemod.config.server;

import com.magicscience.magicsciencemod.config.server.attribute.AttributeConfig;
import com.magicscience.magicsciencemod.config.server.core.CoreConfig;
import com.magicscience.magicsciencemod.config.server.structure.StructureConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static final ForgeConfigSpec SPEC;

    static {
        var builder = new ForgeConfigSpec.Builder();

        CoreConfig.register(builder);
        AttributeConfig.register(builder);
        StructureConfig.register(builder);

        SPEC = builder.build();
    }
}

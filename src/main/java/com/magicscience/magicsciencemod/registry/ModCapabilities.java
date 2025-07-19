package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.mana.IMana;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {
    public static final Capability<IMana> MANA_CAPABILITY = net.minecraftforge.common.capabilities.CapabilityManager.get(new CapabilityToken<>(){});
}

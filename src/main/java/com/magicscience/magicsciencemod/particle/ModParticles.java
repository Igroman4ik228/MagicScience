package com.magicscience.magicsciencemod.particle;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, MagicScienceMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> ENERGY_PARTICLE =
            PARTICLE_TYPES.register("energy", () -> new SimpleParticleType(true));

//    public static final RegistryObject<ParticleType<EnergyParticleData>> ENERGY_PARTICLE =
//            PARTICLE_TYPES.register("energy_particle", () ->
//                    new ParticleType<>(false, EnergyParticleData.DESERIALIZER) {
//                        @Override
//                        public Codec<EnergyParticleData> codec() {
//                            return null;
//                        }
//                    });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}

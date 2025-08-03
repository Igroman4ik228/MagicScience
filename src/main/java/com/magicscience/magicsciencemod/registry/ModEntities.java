package com.magicscience.magicsciencemod.registry;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.magicscience.magicsciencemod.entities.MagicEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MagicScienceMod.MOD_ID);

    public static final RegistryObject<EntityType<MagicEntity>> MAGIC_ENTITY =
        ENTITIES.register("magic_entity",
            () -> EntityType.Builder.<MagicEntity>of(MagicEntity::new, MobCategory.CREATURE)
                .sized(0.1f, 0.1f)
                .clientTrackingRange(64)
                .updateInterval(1)
                .noSave()
                .setShouldReceiveVelocityUpdates(false)
                .build("magic_entity")
        );

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}

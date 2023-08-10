package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.DuckBossEntity;
import net.quackimpala7321.duckmod.entity.DuckEntity;
import net.quackimpala7321.duckmod.entity.DuckFeatherEntity;

public class ModEntities {
    public static final EntityType<DuckEntity> DUCK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(DuckMod.MOD_ID, "duck"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DuckEntity::new).dimensions(EntityDimensions.fixed(0.4f, 0.7f)).build()
    );

    public static final EntityType<DuckBossEntity> DUCK_BOSS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(DuckMod.MOD_ID, "duck_boss"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DuckBossEntity::new).dimensions(EntityDimensions.fixed(4.5f * 0.4f, 4.5f * 0.7f)).build()
    );

    public static final EntityType<DuckFeatherEntity> DUCK_FEATHER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(DuckMod.MOD_ID, "duck_feather"),
            FabricEntityTypeBuilder.<DuckFeatherEntity>create(SpawnGroup.MISC, DuckFeatherEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(DUCK, DuckEntity.createDuckAttributes());
        FabricDefaultAttributeRegistry.register(DUCK_BOSS, DuckBossEntity.createDuckBossAttributes());
    }

    public static void registerModEntities() {
        registerAttributes();

        DuckMod.LOGGER.info("Registering Entities for " + DuckMod.MOD_ID);
    }
}

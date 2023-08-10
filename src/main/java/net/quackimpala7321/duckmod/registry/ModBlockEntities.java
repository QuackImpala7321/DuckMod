package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.block.DuckBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<DuckBlockEntity> DUCK_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(DuckMod.MOD_ID, "duck_block"),
            FabricBlockEntityTypeBuilder.create(DuckBlockEntity::new, ModBlocks.DUCK_BLOCK).build()
    );

    public static void registerModBlockEntities() {
        DuckMod.LOGGER.info("Registering Block Entities for " + DuckMod.MOD_ID);
    }
}

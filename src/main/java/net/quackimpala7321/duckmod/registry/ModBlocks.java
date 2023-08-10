package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.minecraft.registry.Registry;
import net.quackimpala7321.duckmod.block.DuckBlock;
import net.quackimpala7321.duckmod.block.EggIncubatorBlock;

public class ModBlocks {
    public static final Block DUCK_BLOCK = registerBlock("duck_block",
            new DuckBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK)));
    public static final Block DUCK_ORE = registerBlock("duck_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE)));
    public static final Block DEEPSLATE_DUCK_ORE = registerBlock("deepslate_duck_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_DIAMOND_ORE)));
    public static final Block EGG_INCUBATOR = registerBlock("egg_incubator",
            new EggIncubatorBlock(FabricBlockSettings.copyOf(Blocks.BREWING_STAND)
                    .nonOpaque()
                    .luminance(state -> state.get(EggIncubatorBlock.AGE) >= EggIncubatorBlock.MAX_AGE ? 15 : 5)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(DuckMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(DuckMod.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        DuckMod.LOGGER.info("Registering Blocks for " + DuckMod.MOD_ID);
    }
}

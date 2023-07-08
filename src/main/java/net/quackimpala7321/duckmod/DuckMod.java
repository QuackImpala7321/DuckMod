package net.quackimpala7321.duckmod;

import net.fabricmc.api.ModInitializer;

import net.quackimpala7321.duckmod.block.ModBlockEntities;
import net.quackimpala7321.duckmod.block.ModBlocks;
import net.quackimpala7321.duckmod.config.ModConfig;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.entity.ModEntitySpawns;
import net.quackimpala7321.duckmod.item.ModItemGroups;
import net.quackimpala7321.duckmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuckMod implements ModInitializer {
    public static final String MOD_ID = "duckmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = new ModConfig();
        CONFIG.load();

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModEntities.registerModEntities();
        ModBiomeTags.registerBiomeTags();
        ModEntitySpawns.addSpawns();
        ModItemGroups.registerItemGroups();
        ModSoundEvents.registerSoundEvents();
    }
}
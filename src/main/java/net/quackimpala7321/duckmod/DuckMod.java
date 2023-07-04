package net.quackimpala7321.duckmod;

import net.fabricmc.api.ModInitializer;

import net.quackimpala7321.duckmod.block.ModBlockEntities;
import net.quackimpala7321.duckmod.block.ModBlocks;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.item.ModItemGroups;
import net.quackimpala7321.duckmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuckMod implements ModInitializer {
    public static final String MOD_ID = "duckmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModEntities.registerModEntities();
        ModItemGroups.registerItemGroups();
        ModSoundEvents.registerSoundEvents();
    }
}
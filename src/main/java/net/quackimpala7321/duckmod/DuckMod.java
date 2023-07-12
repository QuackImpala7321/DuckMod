package net.quackimpala7321.duckmod;

import com.google.gson.JsonObject;
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
        CONFIG = new ModConfig(MOD_ID + ".config.json", createDefaultConfig());
        CONFIG.load();

        DuckMod.LOGGER.info("Loading Config for " + MOD_ID);

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModEntities.registerModEntities();
        ModBiomeTags.registerBiomeTags();
        ModEntitySpawns.addSpawns();
        ModItemGroups.registerItemGroups();
        ModSoundEvents.registerSoundEvents();
    }

    private JsonObject createDefaultConfig() {
        JsonObject rootObject = new JsonObject();

        JsonObject duck = new JsonObject();

        duck.addProperty("wild_max_health", 4.0f);
        duck.addProperty("wild_damage", 2.0f);
        duck.addProperty("wild_speed", 0.3f);

        duck.addProperty("tamed_max_health", 20.0f);
        duck.addProperty("tamed_damage", 4.0f);
        duck.addProperty("tamed_speed", 0.35f);

        duck.addProperty("spawn_weight", 8);

        rootObject.add("duck", duck);

        return rootObject;
    }
}
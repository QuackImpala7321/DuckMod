package net.quackimpala7321.duckmod.entity;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.ModBiomeTags;

public class ModEntitySpawns {
    public static void addSpawns() {
        JsonObject duckConfig = DuckMod.CONFIG.root.getAsJsonObject().get("duck").getAsJsonObject();

        BiomeModifications.addSpawn(BiomeSelectors.tag(ModBiomeTags.SPAWNS_DUCKS),
                SpawnGroup.CREATURE,
                ModEntities.DUCK_ENTITY,
                duckConfig.get("duck_spawn_weight").getAsInt(), 1, 4);

        DuckMod.LOGGER.info("Registering Entity Spawns for " + DuckMod.MOD_ID);
    }
}

package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.quackimpala7321.duckmod.DuckMod;

public final class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> DUCK_ORE =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(DuckMod.MOD_ID, "duck_ore"));

    public static void registerPlacedFeatures() {
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, DUCK_ORE);

        DuckMod.LOGGER.info("Registering Placed Features for " + DuckMod.MOD_ID);
    }
}

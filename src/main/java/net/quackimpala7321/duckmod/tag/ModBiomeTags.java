package net.quackimpala7321.duckmod.tag;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.quackimpala7321.duckmod.DuckMod;

public final class ModBiomeTags {
    public static final TagKey<Biome> SPAWNS_DUCKS = ModBiomeTags.of("spawns_ducks");

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(DuckMod.MOD_ID, id));
    }

    public static void registerBiomeTags() {
        DuckMod.LOGGER.info("Registering Biome Tags for " + DuckMod.MOD_ID);
    }
}

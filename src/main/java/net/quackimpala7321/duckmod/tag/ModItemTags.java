package net.quackimpala7321.duckmod.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;

public final class ModItemTags {
    public static final TagKey<Item> CAULDRON_ITEMS = ModItemTags.of("cauldron_items");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(DuckMod.MOD_ID, id));
    }

    public static void registerItemTags() {
        DuckMod.LOGGER.info("Registering Item Tags for " + DuckMod.MOD_ID);
    }
}

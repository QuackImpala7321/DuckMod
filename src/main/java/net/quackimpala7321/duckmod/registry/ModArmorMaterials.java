package net.quackimpala7321.duckmod.registry;

import net.minecraft.item.ArmorMaterial;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.armor.DuckArmorMaterial;
import net.quackimpala7321.duckmod.armor.DuckNestArmorMaterial;

public class ModArmorMaterials {
    public static final ArmorMaterial DUCK_ARMOR_MATERIAL = new DuckArmorMaterial();

    public static final ArmorMaterial DUCK_NEST_ARMOR_MATERIAL = new DuckNestArmorMaterial();

    public static void registerArmorMaterials() {
        DuckMod.LOGGER.info("Registering Armor Materials for " + DuckMod.MOD_ID);
    }
}

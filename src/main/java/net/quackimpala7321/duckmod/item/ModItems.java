package net.quackimpala7321.duckmod.item;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.armor.custom.DuckArmorMaterial;
import net.quackimpala7321.duckmod.armor.custom.DuckNestArmorMaterial;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.item.custom.DuckEggItem;
import net.quackimpala7321.duckmod.item.custom.DuckFeatherItem;
import net.quackimpala7321.duckmod.item.custom.DuckNestItem;


public class ModItems {
    public static final Item DUCK_INGOT = registerItem("duck_ingot",
            new Item(new FabricItemSettings()));

    public static final Item DUCKY_SWORD = registerItem("ducky_sword",
            new SwordItem(ModToolMaterials.DUCK, 3, -2.4f, new FabricItemSettings()));
    public static final Item DUCKY_PICKAXE = registerItem("ducky_pickaxe",
            new PickaxeItem(ModToolMaterials.DUCK, 1, -2.8f, new FabricItemSettings()));
    public static final Item DUCKY_SHOVEL = registerItem("ducky_shovel",
            new ShovelItem(ModToolMaterials.DUCK, 1.5f, -3.0f, new FabricItemSettings()));

    public static final Item COOKED_DUCK = registerItem("cooked_duck",
            new Item(new FabricItemSettings().food(FoodComponents.COOKED_CHICKEN)));
    public static final Item DUCK = registerItem("duck",
            new Item(new FabricItemSettings().food(FoodComponents.CHICKEN)));
    public static final Item DUCK_EGG = registerItem("duck_egg",
            new DuckEggItem(new FabricItemSettings().maxCount(16)));

    public static final Item DUCK_SPAWN_EGG = registerItem("duck_spawn_egg",
            new SpawnEggItem(ModEntities.DUCK_ENTITY, 0x36A312, 0xEFAC28, new FabricItemSettings()));

    private static final ArmorMaterial DUCK_ARMOR_MATERIAL = new DuckArmorMaterial();

    public static final Item DUCK_HELMET = registerItem("duck_helmet",
            new ArmorItem(DUCK_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item DUCK_CHESTPLATE = registerItem("duck_chestplate",
            new ArmorItem(DUCK_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item DUCK_LEGGINGS = registerItem("duck_leggings",
            new ArmorItem(DUCK_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item DUCK_BOOTS = registerItem("duck_boots",
            new ArmorItem(DUCK_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    private static final ArmorMaterial DUCK_NEST_ARMOR_MATERIAL = new DuckNestArmorMaterial();

    public static final Item DUCK_NEST = registerItem("duck_nest",
            new DuckNestItem(DUCK_NEST_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));

    public static final Item DUCK_FEATHER_ITEM = registerItem("duck_feather",
            new DuckFeatherItem(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(DuckMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        DuckMod.LOGGER.info("Registering Items for " + DuckMod.MOD_ID);
    }
}

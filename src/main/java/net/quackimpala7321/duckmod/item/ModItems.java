package net.quackimpala7321.duckmod.item;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;


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

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(DuckMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        DuckMod.LOGGER.info("Registering Items for " + DuckMod.MOD_ID);
    }
}

package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;

public final class ModItemGroups {
    public static final boolean ENABLE_ITEM_GROUPS = DuckMod.CONFIG.getValue("enable_item_groups").getAsBoolean();
    public static final RegistryKey<ItemGroup> DUCK = ModItemGroups.register("duck");

    private static RegistryKey<ItemGroup> register(String name) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(DuckMod.MOD_ID, name));
    }

    public static void registerItemGroups() {
        if(ENABLE_ITEM_GROUPS) {
            Registry.register(Registries.ITEM_GROUP, DUCK, FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.DUCK_INGOT))
                    .displayName(Text.translatable("itemGroup.duckmod.duck"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.DUCK_INGOT);
                        entries.add(ModItems.DUCKY_SWORD);
                        entries.add(ModItems.DUCKY_PICKAXE);
                        entries.add(ModItems.DUCKY_SHOVEL);
                        entries.add(ModItems.DUCK_NEST);
                        entries.add(ModItems.DUCK_HELMET);
                        entries.add(ModItems.DUCK_CHESTPLATE);
                        entries.add(ModItems.DUCK_LEGGINGS);
                        entries.add(ModItems.DUCK_BOOTS);
                        entries.add(ModItems.DUCK);
                        entries.add(ModItems.COOKED_DUCK);
                        entries.add(ModBlocks.DUCK_BLOCK);
                        entries.add(ModBlocks.DUCK_ORE);
                        entries.add(ModBlocks.DEEPSLATE_DUCK_ORE);
                        entries.add(ModBlocks.EGG_INCUBATOR);
                        entries.add(ModItems.DUCK_EGG);
                        entries.add(ModItems.DUCK_SPAWN_EGG);
                        entries.add(ModItems.DUCK_FEATHER);
                        entries.add(ModItems.MYSTERIOUS_EMBLEM);
                    }))
                    .build());
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addBefore(Items.NETHERITE_HELMET, ModItems.DUCK_HELMET));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(ModItems.DUCK_HELMET, ModItems.DUCK_CHESTPLATE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(ModItems.DUCK_CHESTPLATE, ModItems.DUCK_LEGGINGS));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(ModItems.DUCK_LEGGINGS, ModItems.DUCK_BOOTS));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(Items.TURTLE_HELMET, ModItems.DUCK_NEST));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(Items.DIAMOND_SWORD, ModItems.DUCKY_SWORD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.addAfter(Items.DIAMOND_HOE, ModItems.DUCKY_SHOVEL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.addAfter(ModItems.DUCKY_SHOVEL, ModItems.DUCKY_PICKAXE));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> content.addAfter(Items.COOKED_CHICKEN, ModItems.DUCK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> content.addAfter(ModItems.DUCK, ModItems.COOKED_DUCK));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> content.addBefore(Blocks.ANCIENT_DEBRIS, ModBlocks.DUCK_ORE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> content.addAfter(ModBlocks.DUCK_ORE, ModBlocks.DEEPSLATE_DUCK_ORE));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> content.addAfter(Items.DIAMOND, ModItems.DUCK_INGOT));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> content.addAfter(Blocks.DIAMOND_BLOCK, ModBlocks.DUCK_BLOCK));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> content.addAfter(Items.EGG, ModItems.DUCK_EGG));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> content.addAfter(Items.FEATHER, ModItems.DUCK_FEATHER));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> content.addAfter(Items.CHICKEN_SPAWN_EGG, ModItems.DUCK_SPAWN_EGG));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.addBefore(Blocks.COMPOSTER, ModBlocks.EGG_INCUBATOR));

        DuckMod.LOGGER.info("Registering Item Groups for " + DuckMod.MOD_ID);
    }
}

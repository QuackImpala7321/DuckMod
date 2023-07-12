package net.quackimpala7321.duckmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.block.ModBlocks;

public class ModItemGroups {
    private static final ItemGroup DUCK = Registry.register(Registries.ITEM_GROUP, new Identifier(DuckMod.MOD_ID, "duck"), FabricItemGroup.builder()
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
                        entries.add(ModBlocks.EGG_INCUBATOR);
                        entries.add(ModItems.DUCK_EGG);
                        entries.add(ModItems.DUCK_SPAWN_EGG);
                    }))
            .build());

    public static void registerItemGroups() {
        DuckMod.LOGGER.info("Registering Item Groups for " + DuckMod.MOD_ID);
    }
}

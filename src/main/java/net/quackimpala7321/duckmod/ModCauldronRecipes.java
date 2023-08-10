package net.quackimpala7321.duckmod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.quackimpala7321.crm.CauldronRecipe;
import net.quackimpala7321.duckmod.registry.ModItems;

import java.util.function.Consumer;

public class ModCauldronRecipes implements Consumer<Consumer<CauldronRecipe>> {
    @Override
    public void accept(Consumer<CauldronRecipe> consumer) {
        CauldronRecipe duckIngots = CauldronRecipe.Builder.create()
                .experience(20)
                .input(
                        new ItemStack(ModItems.DUCK_INGOT, 3),
                        new ItemStack(ModItems.DUCK_FEATHER, 3),
                        new ItemStack(ModItems.DUCK_EGG, 3)
                )
                .output(new ItemStack(ModItems.DUCK_INGOT, 5))
                .build(consumer, new Identifier(DuckMod.MOD_ID, "duck_ingot_from_cauldron"));

        CauldronRecipe duckNest = CauldronRecipe.Builder.create()
                .experience(30)
                .input(
                        new ItemStack(Items.STICK, 5),
                        new ItemStack(ModItems.DUCK_EGG, 3)
                )
                .output(new ItemStack(ModItems.DUCK_NEST))
                .build(consumer, new Identifier(DuckMod.MOD_ID, "duck_nest_from_cauldron"));

        CauldronRecipe mysteriousEmblem = CauldronRecipe.Builder.create()
                .experience(40)
                .input(
                        new ItemStack(ModItems.DUCK_EGG, 10),
                        new ItemStack(ModItems.DUCK_INGOT, 20),
                        new ItemStack(Items.DIAMOND, 2)
                )
                .output(new ItemStack(Items.EGG))
                .build(consumer, new Identifier(DuckMod.MOD_ID, "mysterious_emblem_from_cauldron"));
    }
}

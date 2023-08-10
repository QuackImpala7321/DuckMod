package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.quackimpala7321.crm.CauldronRecipe;
import net.quackimpala7321.crm.util.CauldronRecipeProvider;
import net.quackimpala7321.duckmod.ModCauldronRecipes;

import java.util.function.Consumer;

public class ModCauldronRecipeProvider extends CauldronRecipeProvider {
    public ModCauldronRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateRecipe(Consumer<CauldronRecipe> consumer) {
        new ModCauldronRecipes().accept(consumer);
    }
}

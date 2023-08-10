package net.quackimpala7321.duckmod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.quackimpala7321.duckmod.registry.ModAdvancementsProvider;
import net.quackimpala7321.duckmod.registry.ModCauldronRecipeProvider;

public class ModDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModAdvancementsProvider::new);
        pack.addProvider(ModCauldronRecipeProvider::new);
    }
}

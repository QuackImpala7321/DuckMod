package net.quackimpala7321.duckmod.entity.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.entity.model.custom.DuckEntityModel;
import net.quackimpala7321.duckmod.entity.renderer.custom.DuckEntityRenderer;

public class ModEntityRenderers {
    public static final EntityModelLayer DUCK_MODEL_LAYER = new EntityModelLayer(new Identifier(DuckMod.MOD_ID, "duck"), "main");

    public static void registerModEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.DUCK_ENTITY, ctx -> {
            return new DuckEntityRenderer(ctx);
        });

        EntityModelLayerRegistry.registerModelLayer(DUCK_MODEL_LAYER, DuckEntityModel::getTexturedModelData);

        DuckMod.LOGGER.info("Registering Entity Renderers for " + DuckMod.MOD_ID);
    }
}

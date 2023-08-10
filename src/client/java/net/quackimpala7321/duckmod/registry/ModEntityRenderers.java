package net.quackimpala7321.duckmod.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.model.DuckBossEntityModel;
import net.quackimpala7321.duckmod.entity.model.DuckEntityModel;
import net.quackimpala7321.duckmod.entity.renderer.DuckBossEntityRenderer;
import net.quackimpala7321.duckmod.entity.renderer.DuckEntityRenderer;
import net.quackimpala7321.duckmod.entity.renderer.DuckFeatherEntityRenderer;

public class ModEntityRenderers {
    public static final EntityModelLayer DUCK_MODEL_LAYER = registerModelLayer("duck");
    public static final EntityModelLayer DUCK_BOSS_MODEL_LAYER = registerModelLayer("duck_boss");

    public static void registerModEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.DUCK, DuckEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DUCK_BOSS, DuckBossEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.DUCK_FEATHER, DuckFeatherEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DUCK_MODEL_LAYER, DuckEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DUCK_BOSS_MODEL_LAYER, DuckBossEntityModel::getTexturedModelData);

        DuckMod.LOGGER.info("Registering Entity Renderers for " + DuckMod.MOD_ID);
    }

    private static EntityModelLayer registerModelLayer(String name) {
        return new EntityModelLayer(new Identifier(DuckMod.MOD_ID, name), "main");
    }
}

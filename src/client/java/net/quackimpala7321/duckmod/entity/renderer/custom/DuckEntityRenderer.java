package net.quackimpala7321.duckmod.entity.renderer.custom;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import net.quackimpala7321.duckmod.entity.renderer.ModEntityRenderers;

public class DuckEntityRenderer extends MobEntityRenderer<DuckEntity, ChickenEntityModel<DuckEntity>> {
    private static final Identifier TEXTURE = new Identifier(DuckMod.MOD_ID, "textures/entity/duck.png");

    public DuckEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ChickenEntityModel(context.getPart(ModEntityRenderers.DUCK_MODEL_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTexture(DuckEntity duckEntity) {
        return TEXTURE;
    }

    @Override
    protected float getAnimationProgress(DuckEntity duckEntity, float f) {
        float g = MathHelper.lerp(f, duckEntity.prevFlapProgress, duckEntity.flapProgress);
        float h = MathHelper.lerp(f, duckEntity.prevMaxWingDeviation, duckEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0f) * h;
    }
}

package net.quackimpala7321.duckmod.entity.renderer.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import net.quackimpala7321.duckmod.entity.model.custom.DuckEntityModel;
import net.quackimpala7321.duckmod.entity.renderer.ModEntityRenderers;
import net.quackimpala7321.duckmod.util.CameraUtil;

public class DuckEntityRenderer extends MobEntityRenderer<DuckEntity, DuckEntityModel<DuckEntity>> {
    private static final Identifier TEXTURE = new Identifier(DuckMod.MOD_ID, "textures/entity/duck.png");

    public DuckEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DuckEntityModel<>(context.getPart(ModEntityRenderers.DUCK_MODEL_LAYER)), 0.3f);
    }

    @Override
    public boolean shouldRender(DuckEntity duckEntity, Frustum frustum, double d, double e, double f) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if(duckEntity.isOnOwner() && duckEntity.isOwner(player)) {
            return CameraUtil.getCamera().isThirdPerson();
        }

        return super.shouldRender(duckEntity, frustum, d, e, f);
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

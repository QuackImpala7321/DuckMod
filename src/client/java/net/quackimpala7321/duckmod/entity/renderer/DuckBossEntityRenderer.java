package net.quackimpala7321.duckmod.entity.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.DuckBossEntity;
import net.quackimpala7321.duckmod.entity.model.DuckBossEntityModel;
import net.quackimpala7321.duckmod.registry.ModEntityRenderers;

public class DuckBossEntityRenderer extends MobEntityRenderer<DuckBossEntity, DuckBossEntityModel<DuckBossEntity>> {
    private static final Identifier TEXTURE = new Identifier(DuckMod.MOD_ID, "textures/entity/duck_boss.png");

    public DuckBossEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DuckBossEntityModel<>(context.getPart(ModEntityRenderers.DUCK_BOSS_MODEL_LAYER)), 2f);
    }

    @Override
    protected void scale(DuckBossEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(4.5f, 4.5f, 4.5f);
    }

    @Override
    public Identifier getTexture(DuckBossEntity entity) {
        return TEXTURE;
    }
}

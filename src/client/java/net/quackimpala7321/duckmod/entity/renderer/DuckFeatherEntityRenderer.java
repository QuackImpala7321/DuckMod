package net.quackimpala7321.duckmod.entity.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.DuckFeatherEntity;

public class DuckFeatherEntityRenderer extends ProjectileEntityRenderer<DuckFeatherEntity> {
    public static final Identifier TEXTURE = new Identifier(DuckMod.MOD_ID, "textures/entity/projectiles/duck_feather.png");

    public DuckFeatherEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(DuckFeatherEntity entity) {
        return TEXTURE;
    }
}

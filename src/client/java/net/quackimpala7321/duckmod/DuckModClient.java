package net.quackimpala7321.duckmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.quackimpala7321.duckmod.block.ModBlocks;

public class DuckModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        renderBlocks();
    }

    private void renderBlocks() {
        renderBlock(ModBlocks.EGG_INCUBATOR, RenderLayer.getTranslucent());
    }

    private void renderBlock(Block block, RenderLayer renderLayer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }
}
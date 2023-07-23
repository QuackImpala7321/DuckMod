package net.quackimpala7321.duckmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.quackimpala7321.duckmod.block.ModBlocks;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import net.quackimpala7321.duckmod.entity.renderer.ModEntityRenderers;

import java.util.UUID;

public class DuckModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        renderBlocks();
        ModEntityRenderers.registerModEntityRenderers();
        ClientModParticles.registerClientParticles();

        ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.DUCK_MOUNT_TOGGLE_ID, ((client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            boolean mount = buf.readBoolean();

            client.execute(() -> {
                if(client.world == null) return;

                DuckEntity duckEntity = (DuckEntity) client.world.getEntityById(id);
                if(duckEntity == null) return;

                if(mount) {
                    duckEntity.sitOnOwner();
                } else {
                    duckEntity.dismountOwner();
                }
            });
        }));
    }

    private void renderBlocks() {
        renderBlock(ModBlocks.EGG_INCUBATOR, RenderLayer.getTranslucent());
    }

    private void renderBlock(Block block, RenderLayer renderLayer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }
}
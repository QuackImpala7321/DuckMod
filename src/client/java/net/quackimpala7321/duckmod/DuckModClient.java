package net.quackimpala7321.duckmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.quackimpala7321.duckmod.registry.ClientModParticles;
import net.quackimpala7321.duckmod.registry.ModBlocks;
import net.quackimpala7321.duckmod.entity.DuckEntity;
import net.quackimpala7321.duckmod.registry.ModEntityRenderers;
import net.quackimpala7321.duckmod.registry.ModNetworkingConstants;

public class DuckModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DuckModClient.renderBlocks();
        ModEntityRenderers.registerModEntityRenderers();
        ClientModParticles.registerClientParticles();

        ClientModKeyBinds.registerKeyBinds();
        DuckModClient.registerReceivers();
    }

    private static void registerReceivers() {
        // Duck mounts on player
        ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.DUCK_MOUNT_TOGGLE_ID, (client, handler, buf, responseSender) -> {
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
        });

        // Set gliding
        ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.DUCK_GLIDE_ID, (client, handler, buf, responseSender) -> {
            boolean gliding = buf.readBoolean();

            client.execute(() -> {
                PlayerMixinAccessor playerMixinAccessor = (PlayerMixinAccessor) client.player;

                playerMixinAccessor.setGliding(gliding);
            });
        });
    }

    private static void renderBlocks() {
        renderBlock(ModBlocks.EGG_INCUBATOR, RenderLayer.getTranslucent());
    }

    private static void renderBlock(Block block, RenderLayer renderLayer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }
}
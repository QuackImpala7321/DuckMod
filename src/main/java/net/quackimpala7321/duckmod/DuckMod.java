package net.quackimpala7321.duckmod;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.quackimpala7321.duckmod.config.ModConfig;
import net.quackimpala7321.duckmod.entity.DuckEntity;
import net.quackimpala7321.duckmod.registry.*;
import net.quackimpala7321.duckmod.tag.ModBiomeTags;
import net.quackimpala7321.duckmod.tag.ModItemTags;
import net.quackimpala7321.duckmod.util.KeyBindConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DuckMod implements ModInitializer {
    public static final String MOD_ID = "duckmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        DuckMod.registerModConfig();
        DuckMod.registerConstants();

        ModNetworkingConstants.registerNetworkingConstants();
        ModTrackedData.registerTrackedData();

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModEntities.registerModEntities();

        ModArmorMaterials.registerArmorMaterials();
        ModEntitySpawns.addSpawns();
        ModItemTags.registerItemTags();
        ModBiomeTags.registerBiomeTags();
        ModItemGroups.registerItemGroups();

        ModStatusEffects.registerStatusEffects();
        ModSoundEvents.registerSoundEvents();
        ModParticles.registerParticles();

        ModCriteria.registerCriteria();
        ModPlacedFeatures.registerPlacedFeatures();

        DuckMod.registerReceivers();
    }

    private static void registerModConfig() {
        CONFIG = new ModConfig(MOD_ID + ".config.json", createDefaultConfig());
        CONFIG.load();

        LOGGER.info("Loading Config for " + MOD_ID);
    }

    private static JsonObject createDefaultConfig() {
        JsonObject rootObject = new JsonObject();

        JsonObject duck = new JsonObject();

        duck.addProperty("wild_max_health", 4.0f);
        duck.addProperty("wild_damage", 2.0f);
        duck.addProperty("wild_speed", 0.3f);

        duck.addProperty("tamed_max_health", 20.0f);
        duck.addProperty("tamed_damage", 4.0f);
        duck.addProperty("tamed_speed", 0.35f);

        duck.addProperty("spawn_weight", 8);

        rootObject.add("duck", duck);

        rootObject.addProperty("enable_item_groups", true);
        rootObject.addProperty("enable_gliding", true);

        return rootObject;
    }

    private static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.DUCK_GLIDE_ID, (server, player, handler, buf, responseSender) -> {
            boolean gliding = buf.readBoolean();

            server.execute(() -> {
                PlayerMixinAccessor playerMixinAccessor = (PlayerMixinAccessor) player;

                playerMixinAccessor.setGliding(gliding);
            });
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerMixinAccessor playerMixinAccessor = (PlayerMixinAccessor) handler.player;

            PacketByteBuf buf = PacketByteBufs.create();
            if(!KeyBindConstants.ENABLE_GLIDING) {
                playerMixinAccessor.setGliding(false);
            }
            buf.writeBoolean(playerMixinAccessor.isGliding());

            ServerPlayNetworking.send(handler.player, ModNetworkingConstants.DUCK_GLIDE_ID, buf);
        });
    }

    private static void registerConstants() {
        KeyBindConstants.registerConstants();
    }
}
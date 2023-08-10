package net.quackimpala7321.duckmod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.quackimpala7321.duckmod.registry.ModNetworkingConstants;
import net.quackimpala7321.duckmod.util.KeyBindConstants;
import org.lwjgl.glfw.GLFW;

public class ClientModKeyBinds {
    public static final String DUCK_MOD_CATEGORY = "key.categories.duckmod.duck";

    private static boolean glideButtonPressed = false;

    public static KeyBinding glideButton = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.duckmod.duck",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            ClientModKeyBinds.DUCK_MOD_CATEGORY
    ));

    public static void registerKeyBinds() {
         if(KeyBindConstants.ENABLE_GLIDING) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (glideButton.wasPressed() && !glideButtonPressed) {
                    PlayerMixinAccessor playerMixinAccessor = (PlayerMixinAccessor) client.player;

                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBoolean(!playerMixinAccessor.isGliding());

                    playerMixinAccessor.setGliding(!playerMixinAccessor.isGliding());
                    ClientPlayNetworking.send(ModNetworkingConstants.DUCK_GLIDE_ID, buf);
                    glideButtonPressed = true;
                }
                glideButtonPressed = false;
            });
        }

        DuckMod.LOGGER.info("Registering Key Binds for " + DuckMod.MOD_ID);
    }
}

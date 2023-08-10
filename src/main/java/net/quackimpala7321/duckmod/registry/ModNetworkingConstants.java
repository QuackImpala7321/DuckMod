package net.quackimpala7321.duckmod.registry;

import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;

public final class ModNetworkingConstants {
    public static final Identifier DUCK_MOUNT_TOGGLE_ID = new Identifier(DuckMod.MOD_ID, "duck_mount_toggle");

    public static final Identifier DUCK_GLIDE_ID = new Identifier(DuckMod.MOD_ID, "test");

    public static void registerNetworkingConstants() {
        DuckMod.LOGGER.info("Registering packets for " + DuckMod.MOD_ID);
    }
}

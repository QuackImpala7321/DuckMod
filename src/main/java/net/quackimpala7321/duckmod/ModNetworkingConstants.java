package net.quackimpala7321.duckmod;

import net.minecraft.util.Identifier;

public class ModNetworkingConstants {
    public static final Identifier DUCK_MOUNT_TOGGLE_ID = new Identifier(DuckMod.MOD_ID, "duck_mount_toggle");

    public static void registerNetworkingConstants() {
        DuckMod.LOGGER.info("Registering packets for " + DuckMod.MOD_ID);
    }
}

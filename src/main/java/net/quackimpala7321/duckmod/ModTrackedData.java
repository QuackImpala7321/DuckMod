package net.quackimpala7321.duckmod;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;

public class ModTrackedData {
    public static final TrackedDataHandler<DuckEntity.SitPosition> SITTING_POSITON = TrackedDataHandler.ofEnum(DuckEntity.SitPosition.class);

    public static void registerTrackedData() {
        TrackedDataHandlerRegistry.register(SITTING_POSITON);

        DuckMod.LOGGER.info("Registering Tracked Data for " + DuckMod.MOD_ID);
    }
}

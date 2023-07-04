package net.quackimpala7321.duckmod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final Identifier DUCK_QUACK_ID = new Identifier(DuckMod.MOD_ID, "duck_quack");
    public static SoundEvent DUCK_QUACK_SOUND_EVENT = SoundEvent.of(DUCK_QUACK_ID);

    public static void registerSoundEvents() {
        Registry.register(Registries.SOUND_EVENT,
                DUCK_QUACK_ID,
                DUCK_QUACK_SOUND_EVENT);

        DuckMod.LOGGER.info("Registering Sound Events for " + DuckMod.MOD_ID);
    }
}

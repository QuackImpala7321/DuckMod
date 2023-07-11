package net.quackimpala7321.duckmod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final Identifier DUCK_QUACK_ID = new Identifier(DuckMod.MOD_ID, "duck_quack");
    public static SoundEvent DUCK_QUACK = SoundEvent.of(DUCK_QUACK_ID);

    public static final Identifier DUCK_HURT_ID = new Identifier(DuckMod.MOD_ID, "duck_hurt");
    public static SoundEvent DUCK_HURT = SoundEvent.of(DUCK_HURT_ID);

    public static final Identifier DUCK_DEATH_ID = new Identifier(DuckMod.MOD_ID, "duck_death");
    public static SoundEvent DUCK_DEATH = SoundEvent.of(DUCK_DEATH_ID);

    public static final Identifier ITEM_ARMOR_EQUIP_DUCK_ID = new Identifier(DuckMod.MOD_ID, "item.armor.equip_duck");
    public static SoundEvent ITEM_ARMOR_EQUIP_DUCK = SoundEvent.of(ITEM_ARMOR_EQUIP_DUCK_ID);

    public static void registerSoundEvents() {
        register(DUCK_QUACK_ID, DUCK_QUACK);
        register(DUCK_HURT_ID, DUCK_HURT);
        register(DUCK_DEATH_ID, DUCK_DEATH);

        DuckMod.LOGGER.info("Registering Sound Events for " + DuckMod.MOD_ID);
    }

    private static void register(Identifier id, SoundEvent event) {
        Registry.register(Registries.SOUND_EVENT,
                id,
                event);
    }
}

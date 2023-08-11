package net.quackimpala7321.duckmod.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;

public final class ModSoundEvents {
    public static final Identifier DUCK_QUACK_ID = new Identifier(DuckMod.MOD_ID, "duck_quack");
    public static SoundEvent DUCK_QUACK = SoundEvent.of(DUCK_QUACK_ID);

    public static final Identifier DUCK_HURT_ID = new Identifier(DuckMod.MOD_ID, "duck_hurt");
    public static SoundEvent DUCK_HURT = SoundEvent.of(DUCK_HURT_ID);

    public static final Identifier DUCK_DEATH_ID = new Identifier(DuckMod.MOD_ID, "duck_death");
    public static SoundEvent DUCK_DEATH = SoundEvent.of(DUCK_DEATH_ID);

    public static final Identifier ITEM_ARMOR_EQUIP_DUCK_ID = new Identifier(DuckMod.MOD_ID, "item.armor.equip_duck");
    public static SoundEvent ITEM_ARMOR_EQUIP_DUCK = SoundEvent.of(ITEM_ARMOR_EQUIP_DUCK_ID);

    public static final Identifier DUCK_BOSS_QUACK_ID = new Identifier(DuckMod.MOD_ID, "duck_boss_quack");
    public static SoundEvent DUCK_BOSS_QUACK = SoundEvent.of(DUCK_BOSS_QUACK_ID);

    public static final Identifier DUCK_BOSS_HURT_ID = new Identifier(DuckMod.MOD_ID, "duck_boss_hurt");
    public static SoundEvent DUCK_BOSS_HURT = SoundEvent.of(DUCK_BOSS_HURT_ID);

    public static final Identifier DUCK_BOSS_DEATH_ID = new Identifier(DuckMod.MOD_ID, "duck_boss_death");
    public static SoundEvent DUCK_BOSS_DEATH = SoundEvent.of(DUCK_BOSS_DEATH_ID);

    public static void registerSoundEvents() {
        ModSoundEvents.register(DUCK_QUACK_ID, DUCK_QUACK);
        ModSoundEvents.register(DUCK_HURT_ID, DUCK_HURT);
        ModSoundEvents.register(DUCK_DEATH_ID, DUCK_DEATH);

        ModSoundEvents.register(DUCK_BOSS_QUACK_ID, DUCK_BOSS_QUACK);
        ModSoundEvents.register(DUCK_BOSS_HURT_ID, DUCK_BOSS_HURT);
        ModSoundEvents.register(DUCK_BOSS_DEATH_ID, DUCK_BOSS_DEATH);

        ModSoundEvents.register(ITEM_ARMOR_EQUIP_DUCK_ID, ITEM_ARMOR_EQUIP_DUCK);

        DuckMod.LOGGER.info("Registering Sound Events for " + DuckMod.MOD_ID);
    }

    private static void register(Identifier id, SoundEvent event) {
        Registry.register(Registries.SOUND_EVENT,
                id,
                event);
    }
}

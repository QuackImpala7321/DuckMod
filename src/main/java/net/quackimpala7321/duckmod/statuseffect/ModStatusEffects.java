package net.quackimpala7321.duckmod.statuseffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;

public class ModStatusEffects {
    public static final StatusEffect DUCK_LEADER = new ModStatusEffectProvider(
            StatusEffectCategory.BENEFICIAL, 0x0D520B);

    public static void registerStatusEffects() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(DuckMod.MOD_ID, "duck_leader"), DUCK_LEADER);
    }
}

package net.quackimpala7321.duckmod;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final DefaultParticleType DUCK_SPLASH = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE,
                new Identifier(DuckMod.MOD_ID, "duck_splash"), DUCK_SPLASH);
    }
}

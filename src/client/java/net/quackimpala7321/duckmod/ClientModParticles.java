package net.quackimpala7321.duckmod;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.quackimpala7321.duckmod.registry.ModParticles;

public class ClientModParticles {
    public static void registerClientParticles() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.DUCK_SPLASH, DuckSplashParticle.Factory::new);
    }
}

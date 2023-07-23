package net.quackimpala7321.duckmod;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class DuckSplashParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public DuckSplashParticle(ClientWorld world, SpriteProvider spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.spriteProvider = spriteProvider;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.9;
        this.velocityY = (this.age < this.maxAge / 2) ? 0.1 : -0.1;
        this.velocityZ *= 0.9;
        this.setSpriteForAge(spriteProvider);
        this.alpha = (float) (this.maxAge - this.age) / this.maxAge;
        this.alpha = Math.max(this.alpha, 0.2f);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            DuckSplashParticle duckSplashParticle = new DuckSplashParticle(world, spriteProvider, x, y, z, velocityX, velocityY, velocityZ);
            duckSplashParticle.setSpriteForAge(this.spriteProvider);
            duckSplashParticle.scale *= 1.3f;
            duckSplashParticle.maxAge = 10;
            return duckSplashParticle;
        }
    }
}

package net.quackimpala7321.duckmod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.quackimpala7321.duckmod.ModSoundEvents;

import java.util.List;
import java.util.function.Predicate;

public class DuckPounceGoal extends PounceAtTargetGoal {
    private final TameableEntity mob;
    private LivingEntity target;
    private final float velocity;
    private float damage;

    private static final Predicate<Entity> ALLOWED = Entity::isAlive;

    public DuckPounceGoal(TameableEntity mob, float velocity, float damage) {
        super(mob, velocity);
        this.mob = mob;
        this.velocity = velocity;
        this.damage = damage;
    }

    @Override
    public void stop() {
        groundPound();
    }

    private void groundPound() {
        ServerWorld serverWorld = (ServerWorld)this.mob.getWorld();

        serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                this.mob.getX(), this.mob.getY(), this.mob.getZ(), 1,
                0, 0, 0, 0);

        if(!this.mob.getWorld().isClient) {
            this.mob.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.3f, 1f);
        }

        List<LivingEntity> entities = this.mob.getWorld()
                .getEntitiesByClass(LivingEntity.class, this.mob.getBoundingBox().expand(3.5), ALLOWED);

        for (LivingEntity livingEntity: entities) {
            knockBack(livingEntity);

            if(mob.isOwner(livingEntity)
            || livingEntity == mob
            || mob.isTeammate(livingEntity))
                continue;

            livingEntity.damage(this.mob.getWorld().getDamageSources().mobAttack(this.mob), this.mob.isTamed() ? 4.0f : 2.0f);
        }
    }

    private void knockBack(Entity entity) {
        double d = entity.getX() - this.mob.getX();
        double e = entity.getZ() - this.mob.getZ();
        double f = Math.max(d * d + e * e, 0.001);
        entity.addVelocity(d / f * 2.0, 0.1, e / f * 2.0);
    }

    @Override
    public boolean canStop() {
        return this.mob.isOnGround();
    }
}

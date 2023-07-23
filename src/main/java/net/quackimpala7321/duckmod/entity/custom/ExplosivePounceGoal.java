package net.quackimpala7321.duckmod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

import java.util.List;
import java.util.function.Predicate;

public class ExplosivePounceGoal extends PounceAtTargetGoal {
    private final TameableEntity mob;

    private static final Predicate<Entity> ALLOWED = Entity::isAlive;

    public ExplosivePounceGoal(DuckEntity mob, float velocity) {
        super(mob, velocity);
        this.mob = mob;
    }

    @Override
    public void stop() {
        if(this.mob.isSitting()) return;

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

            livingEntity.damage(this.mob.getWorld().getDamageSources().mobAttack(this.mob), (float) this.mob.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
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

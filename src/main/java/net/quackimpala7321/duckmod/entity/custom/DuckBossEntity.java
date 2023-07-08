package net.quackimpala7321.duckmod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DuckBossEntity extends PathAwareEntity implements Monster {
    DuckBossFight fight;
    BlockPos fightOrigin;

    public DuckBossEntity(EntityType<? extends DuckBossEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
    }

    public static DefaultAttributeContainer.Builder createDuckBossAttributes() {
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f);
    }

    public void setFight(DuckBossFight fight) {
        this.fight = fight;
    }
}

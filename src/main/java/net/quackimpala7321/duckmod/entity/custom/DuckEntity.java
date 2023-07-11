package net.quackimpala7321.duckmod.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.ModSoundEvents;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class DuckEntity extends TameableEntity {
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0f;
    private float field_28639 = 1.0f;
    public int eggLayTime = this.random.nextInt(6000) + 6000;

    private static final Item TAMING_ITEM = Items.MUSHROOM_STEW;

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(
            Items.BREAD
    );
    private static final Ingredient TEMPT_INGREDIENT = Ingredient.ofItems(
            Items.BREAD,
            TAMING_ITEM
    );

    public static final float WILD_MAX_HEALTH = DuckMod.CONFIG.getValue("duck.wild_max_health").getAsFloat();
    public static final float WILD_DAMAGE = DuckMod.CONFIG.getValue("duck.wild_damage").getAsFloat();
    public static final float WILD_SPEED = DuckMod.CONFIG.getValue("duck.wild_speed").getAsFloat();

    public static final float TAMED_MAX_HEALTH = DuckMod.CONFIG.getValue("duck.tamed_max_health").getAsFloat();
    public static final float TAMED_DAMAGE = DuckMod.CONFIG.getValue("duck.tamed_damage").getAsFloat();
    public static final float TAMED_SPEED = DuckMod.CONFIG.getValue("duck.tamed_speed").getAsFloat();

    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(DuckEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DuckEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);

        setTamed(false);
        setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        setPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0f);
        setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0f);
    }

    public static DefaultAttributeContainer.Builder createDuckAttributes() {
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, WILD_MAX_HEALTH)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, WILD_DAMAGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, WILD_SPEED);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new DuckPounceGoal(this, 0.6f));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.add(5, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(6, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(7, new TemptGoal(this, 1.0, TEMPT_INGREDIENT, false));
        this.goalSelector.add(8, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(9, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(item == TAMING_ITEM && !isTamed()) {
            if(getWorld().isClient) {
                return ActionResult.CONSUME;
            } else {
                if(!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                setOwner(player);
                setTamed(true);
                this.navigation.recalculatePath();
                this.setTarget(null);
                getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                setSit(true);

                return ActionResult.SUCCESS;
            }
        }

        if(isTamed() && !getWorld().isClient && isOwner(player) && hand == Hand.MAIN_HAND) {
            setSit(!isSitting());
            return ActionResult.SUCCESS;
        }

        if(item == TAMING_ITEM)
            return ActionResult.PASS;

        return super.interactMob(player, hand);
    }

    public float getDamage() {
        return (float)getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    @Override
    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if (target instanceof CreeperEntity || target instanceof GhastEntity) {
            return false;
        }
        if (target instanceof DuckEntity) {
            DuckEntity duckEntity = (DuckEntity) target;
            return !duckEntity.isTamed() || duckEntity.getOwner() != owner;
        }
        if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).shouldDamagePlayer((PlayerEntity)target)) {
            return false;
        }
        if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
            return false;
        }
        return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(TAMED_MAX_HEALTH);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(TAMED_SPEED);
            this.setHealth(TAMED_MAX_HEALTH);
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(WILD_MAX_HEALTH);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(WILD_DAMAGE);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(WILD_SPEED);
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0f : 4.0f) * 0.3f;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0f, 1.0f);
        if (!this.isOnGround() && this.flapSpeed < 1.0f) {
            this.flapSpeed = 1.0f;
        }
        this.flapSpeed *= 0.9f;
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, isTamed() && getTarget() != null ? 1.0 : 0.6, 1.0));
        }
        this.flapProgress += this.flapSpeed * 2.0f;
        if (!this.getWorld().isClient && this.isAlive() && !this.isBaby() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.dropItem(ModItems.DUCK_EGG);
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("EggLayTime")) {
            this.eggLayTime = nbt.getInt("EggLayTime");
        }
        if(nbt.contains("isSitting")) {
            this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("EggLayTime", this.eggLayTime);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.85f : dimensions.height * 0.92f;
    }

    @Override
    public boolean isTeammate(Entity other) {
        if(!(other instanceof TameableEntity)) {
            return false;
        }
        TameableEntity subject = (TameableEntity)other;

        return this.getOwner() == subject.getOwner();
    }

    @Override
    protected boolean isFlappingWings() {
        return this.speed > this.field_28639;
    }

    @Override
    protected void addFlapEffects() {
        this.field_28639 = this.speed + this.maxWingDeviation / 2.0f;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.DUCK_QUACK;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.DUCK_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.DUCK_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15f, 1.0f);
    }

    @Nullable
    @Override
    public DuckEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return ModEntities.DUCK_ENTITY.create(serverWorld);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return this.isTamed() && super.canBeLeashedBy(player);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}

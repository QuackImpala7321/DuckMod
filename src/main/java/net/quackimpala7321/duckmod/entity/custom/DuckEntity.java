package net.quackimpala7321.duckmod.entity.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
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
import net.quackimpala7321.duckmod.ModNetworkingConstants;
import net.quackimpala7321.duckmod.ModParticles;
import net.quackimpala7321.duckmod.ModSoundEvents;
import net.quackimpala7321.duckmod.advancement.ModCriteria;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.item.ModItems;
import net.quackimpala7321.duckmod.item.custom.DuckFeatherItem;
import net.quackimpala7321.duckmod.statuseffect.ModStatusEffects;
import org.jetbrains.annotations.Nullable;

public class DuckEntity extends TameableEntity implements RangedAttackMob {
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0f;
    private float field_28639 = 1.0f;
    public int eggLayTime = this.random.nextInt(6000) + 6000;
    private boolean left;
    private int splashTicks;

    private int ticks = 0;

    private static final Item TAMING_ITEM = Items.BAKED_POTATO;

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(
            Items.BREAD
    );
    private static final Ingredient TEMPT_INGREDIENT = Ingredient.ofItems(
            Items.BREAD,
            TAMING_ITEM
    );

    private static final TrackedData<Boolean> SITTING_ON_PLAYER =
            DataTracker.registerData(DuckEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> LEFT =
            DataTracker.registerData(DuckEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final float WILD_MAX_HEALTH = DuckMod.CONFIG.getValue("duck.wild_max_health").getAsFloat();
    public static final float WILD_DAMAGE = DuckMod.CONFIG.getValue("duck.wild_damage").getAsFloat();
    public static final float WILD_SPEED = DuckMod.CONFIG.getValue("duck.wild_speed").getAsFloat();

    public static final float TAMED_MAX_HEALTH = DuckMod.CONFIG.getValue("duck.tamed_max_health").getAsFloat();
    public static final float TAMED_DAMAGE = DuckMod.CONFIG.getValue("duck.tamed_damage").getAsFloat();
    public static final float TAMED_SPEED = DuckMod.CONFIG.getValue("duck.tamed_speed").getAsFloat();

    public DuckEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(ModEntities.DUCK_ENTITY, world);

        this.moveControl = new DuckMoveControl(this);
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
        this.goalSelector.add(2, new DuckProjectileAttackGoal(this, 1.0, 30, 20));
        this.goalSelector.add(3, new ExplosivePounceGoal(this, 0.6f));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.add(5, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(8, new SitOnOwnerGoal(this));
        this.goalSelector.add(9, new TemptGoal(this, 1.0, TEMPT_INGREDIENT, false));
        this.goalSelector.add(10, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(11, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(11, new LookAroundGoal(this));
        this.goalSelector.add(12, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(isOwner(player) && isOnOwner()) return ActionResult.PASS;

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

        if(isBreedingItem(itemStack)) {
            if(!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            heal(item.getFoodComponent().getHunger());
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if ((target instanceof CreeperEntity || target instanceof GhastEntity) && !isOnOwner()) {
            return false;
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

    public void setSit(boolean sitting) {
        setInSittingPose(sitting);
        super.setSitting(sitting);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0f : 4.0f) * 0.3f;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0f, 1.0f);
        if(this.isTouchingWater()) {
            this.maxWingDeviation = 0.7f;
        }
        if(this.isOnGround()) {
            this.maxWingDeviation = 0;
        }
        if (!this.isOnGround() && this.flapSpeed < 1.0f) {
            this.flapSpeed = 1.0f;
        }
        this.flapSpeed *= (this.isTouchingWater() ? 0.3f : 0.6f);
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, isTamed() && getTarget() != null ? 1.0 : 0.6, 1.0));
        }
        this.flapProgress += this.flapSpeed * 2.0f;
        if (!this.getWorld().isClient && this.isAlive() && !this.isBaby() && !this.isOnOwner() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.dropItem(ModItems.DUCK_EGG);
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }

        this.splashTicks++;
        if(this.splashTicks >= 3 && getWorld().isClient && !this.isOnOwner() && this.isTouchingWater() && this.isAlive()) {
            spawnSplashParticles(getWorld());
            this.splashTicks = 0;
        }
    }

    private void spawnSplashParticles(World world) {
        for(int i=0; i<3; i++) {
            world.addParticle(ModParticles.DUCK_SPLASH, this.getX(), this.getY(), this.getZ(),
                    0-(this.getVelocity().x + (random.nextFloat() * 0.5) - 0.25), 0.3, 0-(this.getVelocity().z + (random.nextFloat() * 0.5) - 0.25));
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return (damageSource.getTypeRegistryEntry().matchesKey(DamageTypes.IN_WALL) && isOnOwner()) || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING_ON_PLAYER, false);
        this.dataTracker.startTracking(LEFT, true);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("EggLayTime", this.eggLayTime);
        nbt.putBoolean("SittingOnPlayer", this.dataTracker.get(SITTING_ON_PLAYER));
        nbt.putBoolean("Left", this.dataTracker.get(LEFT));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("EggLayTime")) {
            this.eggLayTime = nbt.getInt("EggLayTime");
        }
        if(nbt.contains("SittingOnPlayer")) {
            this.dataTracker.set(SITTING_ON_PLAYER, nbt.getBoolean("SittingOnPlayer"));
        }
        if(nbt.contains("Left")) {
            this.dataTracker.set(LEFT, nbt.getBoolean("Left"));
        }
    }

    @Override
    public void tick() {
        this.ticks++;
        super.tick();
    }

    public double onPlayerHeightOffset() {
        return 0.5;
    }

    public boolean isOnOwner() {
        if(this.getOwner() == null) return false;

        return this.getOwner().getPassengerList().contains(this);
    }

    public boolean isOnLeft() {
        return this.left;
    }

    public void sitOnOwner() {
        if(getOwner() == null) return;

        this.startRiding(this.getOwner());
    }

    public void dismountOwner() {
        this.ticks = 0;
        this.stopRiding();
    }

    @Override
    public boolean startRiding(Entity entity) {
        if(entity instanceof PlayerEntity player && this.isOwner(player)) {
            this.left = player.getPassengerList().stream()
                    .noneMatch(entity1 -> (entity1 instanceof DuckEntity duckEntity && duckEntity.isOnLeft()));

            this.ticks = 100;

            if(!this.getWorld().isClient) {
                ModCriteria.DUCK_ON_HEAD.trigger((ServerPlayerEntity) player, this);
            }

        }
        return super.startRiding(entity);
    }

    @Override
    public void stopRiding() {
        super.stopRiding();
    }

    public boolean canSitOnPlayer() {
        if(this.getOwner() == null) return false;

        return this.ticks > 100;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.85f : dimensions.height * 0.92f;
    }

    @Override
    public boolean isTeammate(Entity other) {
        if(!(other instanceof TameableEntity subject)) {
            return false;
        }

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

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    private PersistentProjectileEntity createFeatherProjectile(LivingEntity entity) {
        DuckFeatherItem featherItem = (DuckFeatherItem) ModItems.DUCK_FEATHER_ITEM;
        PersistentProjectileEntity persistentProjectileEntity = featherItem.createFeatherEntity(entity.getWorld(), entity);
        persistentProjectileEntity.setDamage(entity.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));

        return persistentProjectileEntity;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        PersistentProjectileEntity featherProjectile = this.createFeatherProjectile(this);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - featherProjectile.getY() - getHeightOffset();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        featherProjectile.setVelocity(d, e + g * (double)0.2f, f, 1.6f, 8);

        this.getWorld().spawnEntity(featherProjectile);
    }
}

class DuckMoveControl extends MoveControl {
    private final DuckEntity duckEntity;

    DuckMoveControl(DuckEntity duckEntity) {
        super(duckEntity);
        this.duckEntity = duckEntity;
    }

    private void updateVelocity() {
        if(duckEntity.isTouchingWater()) {
            duckEntity.setMovementSpeed(duckEntity.getMovementSpeed() * 5f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        updateVelocity();
    }
}

class DuckProjectileAttackGoal extends ProjectileAttackGoal {
    private final DuckEntity mob;

    public DuckProjectileAttackGoal(DuckEntity mob, double mobSpeed, int intervalTicks, float maxShootRange) {
        super(mob, mobSpeed, intervalTicks, maxShootRange);

        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && mob.isOnOwner();
    }
}

class SitOnOwnerGoal extends Goal {
    private final DuckEntity mob;
    private ServerPlayerEntity owner;
    private boolean mounted;

    public SitOnOwnerGoal(DuckEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)this.mob.getOwner();
        boolean bl = serverPlayerEntity != null && !serverPlayerEntity.isSpectator() && !serverPlayerEntity.getAbilities().flying && !serverPlayerEntity.isTouchingWater() && !serverPlayerEntity.inPowderSnow && serverPlayerEntity.hasStatusEffect(ModStatusEffects.DUCK_LEADER);
        return !this.mob.isSitting() && bl && this.mob.canSitOnPlayer();
    }

    @Override
    public void start() {
        this.owner = (ServerPlayerEntity)this.mob.getOwner();
        this.mounted = false;
    }

    @Override
    public boolean shouldContinue() {
        return !this.owner.isSneaking() && this.owner.hasStatusEffect(ModStatusEffects.DUCK_LEADER);
    }

    @Override
    public boolean canStop() {
        return !this.mounted || this.owner.isSneaking();
    }

    @Override
    public void tick() {
        if (this.mounted || this.mob.isInSittingPose() || this.mob.isLeashed() || this.owner.isSneaking()) {
            return;
        }
        if (this.mob.getBoundingBox().intersects(this.owner.getBoundingBox())) {
            this.mounted = true;

            mount();
        }
    }

    private void mount() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.mob.getId());
        buf.writeBoolean(true);

        ServerPlayNetworking.send(owner, ModNetworkingConstants.DUCK_MOUNT_TOGGLE_ID, buf);
        this.mob.sitOnOwner();
    }

    private void dismount() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.mob.getId());
        buf.writeBoolean(false);

        ServerPlayNetworking.send(owner, ModNetworkingConstants.DUCK_MOUNT_TOGGLE_ID, buf);
        this.mob.dismountOwner();
    }

    @Override
    public void stop() {
        dismount();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}
package net.quackimpala7321.duckmod.entity;

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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.quackimpala7321.duckmod.*;
import net.quackimpala7321.duckmod.registry.*;
import net.quackimpala7321.duckmod.item.DuckFeatherItem;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class DuckEntity extends TameableEntity implements RangedAttackMob {
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0f;
    private float field_28639 = 1.0f;
    public int eggLayTime = this.random.nextInt(6000) + 6000;
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

    private static final TrackedData<SitPosition> SITTING_POSITION =
            DataTracker.registerData(DuckEntity.class, ModTrackedData.SITTING_POSITON);

    public static final float WILD_MAX_HEALTH = DuckMod.CONFIG.getValue("duck.wild_max_health").getAsFloat();
    public static final float WILD_DAMAGE = DuckMod.CONFIG.getValue("duck.wild_damage").getAsFloat();
    public static final float WILD_SPEED = DuckMod.CONFIG.getValue("duck.wild_speed").getAsFloat();

    public static final float TAMED_MAX_HEALTH = DuckMod.CONFIG.getValue("duck.tamed_max_health").getAsFloat();
    public static final float TAMED_DAMAGE = DuckMod.CONFIG.getValue("duck.tamed_damage").getAsFloat();
    public static final float TAMED_SPEED = DuckMod.CONFIG.getValue("duck.tamed_speed").getAsFloat();

    public DuckEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(ModEntities.DUCK, world);

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
        return !(target instanceof TameableEntity tameableEntity && tameableEntity.getOwner() == this.getOwner());
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
        this.dataTracker.startTracking(SITTING_POSITION, SitPosition.NONE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("EggLayTime", this.eggLayTime);
        nbt.putByte("SitPosition", (byte) this.dataTracker.get(SITTING_POSITION).getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("EggLayTime")) {
            this.eggLayTime = nbt.getInt("EggLayTime");
        }
        if(nbt.contains("SitPosition")) {
            this.dataTracker.set(SITTING_POSITION, SitPosition.getById(nbt.getByte("SitPosition")));
        }
    }

    @Override
    public void tick() {
        this.ticks++;
        super.tick();
    }

    public double onPlayerHeightOffset() {
        return switch (getSittingPosition()) {
            case LEFT, RIGHT -> 0.5;
            case BAG -> -0.75;
            default -> 0.0;
        };
    }

    public boolean isOnOwner() {
        return this.dataTracker.get(SITTING_POSITION) != SitPosition.NONE;
    }

    public SitPosition getSittingPosition() {
        return this.dataTracker.get(SITTING_POSITION);
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
            PlayerMixinAccessor playerMixinAccessor = (PlayerMixinAccessor) player;
            DuckBarManager duckBarManager = playerMixinAccessor.getDuckBarManager();

            List<DuckEntity> ducks = duckBarManager.getDucks();
            List<SitPosition> positions = new ArrayList<SitPosition>();

            ducks.forEach(duckEntity -> positions.add(duckEntity.getSittingPosition()));

            SitPosition target = SitPosition.NONE;
            for(SitPosition sitPosition : Arrays.stream(SitPosition.values()).limit(duckBarManager.getSlots()).toList()) {
                if(!positions.contains(sitPosition)) {
                    target = sitPosition;
                    break;
                }
            }

            if(target == SitPosition.NONE) return false;
            this.dataTracker.set(SITTING_POSITION, target);
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

        if(!this.getWorld().isClient) {
            if(!(this.getOwner() instanceof ServerPlayerEntity serverPlayerEntity)) return;

            if(this.isOnOwner() && serverPlayerEntity.isDisconnected()) {
                this.setSit(true);
            }
        }

        this.dataTracker.set(SITTING_POSITION, SitPosition.NONE);
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
            return super.isTeammate(other);
        }

        return super.isTeammate(other) || this.getOwner() == subject.getOwner();
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
        return ModEntities.DUCK.create(serverWorld);
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
        DuckFeatherItem featherItem = (DuckFeatherItem) ModItems.DUCK_FEATHER;
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

    public enum SitPosition implements StringIdentifiable {
        LEFT("left", 0),
        RIGHT("right", 1),
        BAG("bag", 2),
        NONE("none", -1);

        private final String name;
        private final int id;

        SitPosition(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public static SitPosition getById(int id) {
            Iterator<SitPosition> iterator = Arrays.stream(SitPosition.values())
                    .filter(sitPosition -> sitPosition.getId() == id)
                    .iterator();

            return iterator.hasNext() ? iterator.next() : SitPosition.NONE;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public String asString() {
            return this.name;
        }
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

class ExplosivePounceGoal extends PounceAtTargetGoal {
    private final DuckEntity mob;

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
        if(serverPlayerEntity == null) return false;

        boolean bl = !serverPlayerEntity.isSpectator() && !serverPlayerEntity.getAbilities().flying && !serverPlayerEntity.isTouchingWater() && !serverPlayerEntity.inPowderSnow;
        return !this.mob.isSitting() && bl && this.mob.canSitOnPlayer();
    }

    @Override
    public void start() {
        this.owner = (ServerPlayerEntity)this.mob.getOwner();
        this.mounted = false;
    }

    @Override
    public boolean shouldContinue() {
        return !this.owner.isSneaking() && canSupportDuck(this.mob.getSittingPosition().getId());
    }

    private boolean canSupportDuck(int id) {
        PlayerMixinAccessor playerMixinAccessor = (PlayerMixinAccessor) this.owner;
        return playerMixinAccessor.getDuckBarManager().getSlots() > id;
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
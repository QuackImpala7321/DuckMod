package net.quackimpala7321.duckmod.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.registry.ModBlocks;
import net.quackimpala7321.duckmod.registry.ModEntities;
import net.quackimpala7321.duckmod.registry.ModParticles;
import net.quackimpala7321.duckmod.registry.ModSoundEvents;
import net.quackimpala7321.duckmod.util.BlockUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DuckBossEntity extends HostileEntity {
    private static final TrackedData<BlockPos> FIGHT_ORIGIN =
            DataTracker.registerData(DuckBossEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public DuckBossEntity(EntityType<? extends DuckBossEntity> entityType, World world) {
        super(ModEntities.DUCK_BOSS, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5, true));
        this.targetSelector.add(0, new RevengeGoal(this).setGroupRevenge(DuckEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FIGHT_ORIGIN, BlockPos.ORIGIN);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setFightOrigin(this.scanFightOrigin(ModBlocks.DUCK_BLOCK, 5, this.getWorld()));
        DuckMod.LOGGER.info("Result: " + this.getFightOrigin().toShortString());
        world.toServerWorld().spawnParticles(ParticleTypes.COMPOSTER, this.getFightOrigin().getX() + 0.5, this.getFightOrigin().getY() + 1, this.getFightOrigin().getZ() + 0.5, 10, 0.3, 0.3, 0.3, 1);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public static DefaultAttributeContainer.Builder createDuckBossAttributes() {
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f);
    }

    public BlockPos scanFightOrigin(Block block, int radius, World world) {
        DuckMod.LOGGER.info("Current position: " + this.getBlockPos().toShortString());

        Box box = new Box(this.getBlockPos())
                .expand(radius, 0, radius)
                .withMinY(this.getBlockY() - 2)
                .withMaxY(this.getBlockY() + radius);


        List<BlockPos> blockPosList = BlockUtil.scanBoxForBlock(ModBlocks.DUCK_BLOCK, box, world);
        if (blockPosList.isEmpty()) return this.getBlockPos();

        blockPosList.sort(Comparator.comparing(pos -> this.squaredDistanceTo(Vec3d.of(pos))));
        return blockPosList.get(0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putIntArray("FightOrigin", this.posArray(this.getFightOrigin()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("FightOrigin", NbtElement.INT_ARRAY_TYPE)) {
            this.setFightOrigin(this.fromArray(nbt.getIntArray("FightOrigin")));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.DUCK_BOSS_QUACK;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15f, 1.0f);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.DUCK_BOSS_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.DUCK_BOSS_DEATH;
    }

    private ArrayList<Integer> posArray(BlockPos pos) {
        ArrayList<Integer> posList = new ArrayList<>();

        posList.add(pos.getX());
        posList.add(pos.getY());
        posList.add(pos.getZ());

        return posList;
    }

    private BlockPos fromArray(int[] list) {
        return new BlockPos(list[0], list[1], list[2]);
    }

    private BlockPos getFightOrigin() {
        return this.dataTracker.get(FIGHT_ORIGIN);
    }

    private void setFightOrigin(BlockPos pos) {
        this.dataTracker.set(FIGHT_ORIGIN, pos);
    }
}

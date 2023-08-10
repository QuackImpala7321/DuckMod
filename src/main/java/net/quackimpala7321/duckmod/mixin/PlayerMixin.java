package net.quackimpala7321.duckmod.mixin;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.quackimpala7321.duckmod.DuckBarManager;
import net.quackimpala7321.duckmod.PlayerMixinAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin implements PlayerMixinAccessor {
    @Unique
    private final PlayerEntity thisPlayerEntity = (PlayerEntity) (Object) this;
    @Unique
    private final DuckBarManager duckBarManager = new DuckBarManager(thisPlayerEntity);
    @Unique
    private boolean gliding;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;updateWaterSubmersionState()Z", shift = At.Shift.BEFORE))
    public void tickMixin(CallbackInfo ci) {
        this.duckBarManager.update();
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickMovement()V", shift = At.Shift.AFTER))
    public void tickMovementMixin(CallbackInfo ci) {
        if(!this.isGliding() || thisPlayerEntity.isOnGround() || thisPlayerEntity.isTouchingWater()) return;
        if(!duckBarManager.canGlide()) return;
        if(thisPlayerEntity.hasStatusEffect(StatusEffects.SLOW_FALLING)) return;

        thisPlayerEntity.onLanding();

        Vec3d vel = thisPlayerEntity.getVelocity();
        if(vel.y < -0.1) {
            thisPlayerEntity.setVelocity(vel.x, -0.1, vel.z);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbtMixin(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("duckmod:Gliding", this.isGliding());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbtMixin(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("duckmod:Gliding")) {
            this.setGliding(nbt.getBoolean("duckmod:Gliding"));
        }
    }

    @Override
    public void setGliding(boolean set) {
        this.gliding = set;
    }

    @Override
    public boolean isGliding() {
        return this.gliding;
    }

    @Override
    public DuckBarManager getDuckBarManager() {
        return this.duckBarManager;
    }
}

package net.quackimpala7321.duckmod.mixin;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.quackimpala7321.duckmod.DuckBarManager;
import net.quackimpala7321.duckmod.DuckBarManagerAccessor;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin implements DuckBarManagerAccessor {
    @Unique
    private final PlayerEntity thisPlayerEntity = (PlayerEntity) (Object) this;
    @Unique
    public DuckBarManager duckBarManager = new DuckBarManager(thisPlayerEntity);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;updateWaterSubmersionState()Z", shift = At.Shift.BEFORE))
    public void tickMixin(CallbackInfo ci) {
        this.duckBarManager.update();
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickMovement()V", shift = At.Shift.AFTER))
    public void tickMovementMixin(CallbackInfo ci) {
        if(thisPlayerEntity.isOnGround() || thisPlayerEntity.isTouchingWater()) return;
        if(thisPlayerEntity.hasStatusEffect(StatusEffects.SLOW_FALLING)) return;

        int count = this.duckBarManager.getDucks().size();
        if(count < DuckEntity.SitPosition.values().length - 1) return;

        thisPlayerEntity.onLanding();

        Vec3d vel = thisPlayerEntity.getVelocity();
        if(vel.y < -0.1) {
            thisPlayerEntity.setVelocity(vel.x, -0.1, vel.z);
        }
    }

    @Unique
    public DuckBarManager getDuckBarManager() {
        return this.duckBarManager;
    }
}

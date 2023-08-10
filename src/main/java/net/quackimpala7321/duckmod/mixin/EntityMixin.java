package net.quackimpala7321.duckmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.quackimpala7321.duckmod.DuckBarManager;
import net.quackimpala7321.duckmod.PlayerMixinAccessor;
import net.quackimpala7321.duckmod.entity.DuckEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private final Entity thisEntity = (Entity) (Object) this;

    @Inject(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", at = @At("HEAD"), cancellable = true)
    public void updatePassengerPositionMixin(Entity passenger, Entity.PositionUpdater positionUpdater, CallbackInfo ci) {
        if(!(thisEntity instanceof PlayerEntity playerEntity)) return;
        if(!(passenger instanceof DuckEntity duckEntity && duckEntity.isOnOwner())) return;

        double xOffset = switch (duckEntity.getSittingPosition()) {
            case LEFT -> -0.3;
            case RIGHT -> 0.3;
            default -> 0.0;
        };
        double yOffset = playerEntity.getY() + playerEntity.getMountedHeightOffset() + duckEntity.onPlayerHeightOffset() + duckEntity.getHeightOffset();
        double zOffset = duckEntity.getSittingPosition() == DuckEntity.SitPosition.BAG ? 0.5 : 0.0;

        Vec3d vec3d;

        if(duckEntity.getSittingPosition() == DuckEntity.SitPosition.BAG) {
            vec3d = new Vec3d(0.0, 0.0, zOffset).rotateY(-playerEntity.bodyYaw * ((float)Math.PI / 180));
        } else {
            vec3d = new Vec3d(-xOffset, 0.0, 0.0).rotateY(-playerEntity.headYaw * ((float)Math.PI / 180));
        }

        positionUpdater.accept(duckEntity, playerEntity.getX() + vec3d.x, yOffset, playerEntity.getZ() + vec3d.z);

        ci.cancel();
    }

    @Inject(method = "canAddPassenger", at = @At("HEAD"), cancellable = true)
    public void canAddPassengerMixin(Entity passenger, CallbackInfoReturnable<Boolean> cir) {
        Entity thisEntity = (Entity) (Object) this;
        if(!(thisEntity instanceof PlayerMixinAccessor)) return;
        DuckBarManager duckBarManager = ((PlayerMixinAccessor) thisEntity).getDuckBarManager();

        if(passenger instanceof DuckEntity) {
            cir.setReturnValue(duckBarManager.getDucks().size() < duckBarManager.getSlots());
        }
    }
}

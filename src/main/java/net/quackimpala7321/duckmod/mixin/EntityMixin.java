package net.quackimpala7321.duckmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", at = @At("HEAD"), cancellable = true)
    public void updatePassengerPositionMixin(Entity passenger, Entity.PositionUpdater positionUpdater, CallbackInfo ci) {
        Entity thisEntity = (Entity) (Object) this;
        if(!(thisEntity instanceof PlayerEntity playerEntity)) return;
        if(passenger instanceof DuckEntity duckEntity && duckEntity.isOnPlayer()) {
            double xOffset = duckEntity.isOnLeft() ? -0.3 : 0.3;
            double yOffset = playerEntity.getY() + playerEntity.getMountedHeightOffset() + duckEntity.onPlayerHeightOffset() + duckEntity.getHeightOffset();

            Vec3d vec3d = new Vec3d(xOffset, 0.0, 0.0).rotateY(-playerEntity.headYaw * ((float)Math.PI / 180));
            positionUpdater.accept(duckEntity, playerEntity.getX() + vec3d.x, yOffset, playerEntity.getZ() + vec3d.z);

            ci.cancel();
        }
    }
}

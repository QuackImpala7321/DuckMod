package net.quackimpala7321.duckmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;detach()V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void removeMixin(ServerPlayerEntity player, CallbackInfo ci, ServerWorld serverWorld) {
        List<Entity> ducks = player.getPassengerList().stream()
                .filter(entity -> entity instanceof DuckEntity).toList();

        for (Entity entity : ducks) {
            DuckEntity duckEntity = (DuckEntity) entity;
            duckEntity.setSit(true);

            DuckMod.LOGGER.info("Sitting duck " + duckEntity.getUuid());
        }
    }
}

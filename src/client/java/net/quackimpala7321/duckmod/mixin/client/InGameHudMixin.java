package net.quackimpala7321.duckmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckBarManager;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import net.quackimpala7321.duckmod.DuckBarManagerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin{
    @Unique
    private static final Identifier MY_ICONS = new Identifier(DuckMod.MOD_ID, "textures/gui/icons.png");

    @Unique
    private static final String DUCKS = new Identifier(DuckMod.MOD_ID, "ducks").toString();

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderStatusBarsMixin(DrawContext context, CallbackInfo ci, PlayerEntity playerEntity, int ceilingHealth, boolean bl, long l, int j, HungerManager hungerManager, int k, int left, int right, int bottom, float f, int p, int q, int regenIndex, int armorHeight, int airHeight, int playerArmor, int v, LivingEntity livingEntity, int x) {
        if(!(playerEntity instanceof DuckBarManagerAccessor)) return;

        DuckBarManager duckBarManager = ((DuckBarManagerAccessor) playerEntity).getDuckBarManager();

        if(duckBarManager.getSlots() <= 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        client.getProfiler().swap(DUCKS);

        int ducks = (int) playerEntity.getPassengerList().stream()
                .filter(entity -> entity instanceof DuckEntity duckEntity && duckEntity.isAlive() && duckEntity.isOnOwner())
                .count();

        this.renderDuckBar(context, ducks, duckBarManager.getSlots(), left, armorHeight - 9);
    }

    @Unique
    public void renderDuckBar(DrawContext context, int has, int max, int startX, int startY) {
        int x = startX;

        for(int i=1; i<=max; i++) {
            if(i > 1) {
                x += 9;
            }

            if(has >= i) {
                context.drawTexture(MY_ICONS, x, startY, 8, 0, 8, 8);
            } else {
                context.drawTexture(MY_ICONS, x, startY, 0, 0, 8, 8);
            }
        }
    }
}

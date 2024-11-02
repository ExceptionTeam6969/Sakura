package dev.exceptionteam.sakura.mixins.gui;

import dev.exceptionteam.sakura.features.modules.impl.render.GameAnimation;
import dev.exceptionteam.sakura.graphics.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Profilers.get().push("sakura_render2d");

        RenderSystem.INSTANCE.onRender2d(context);

        Profilers.get().pop();
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1), index = 2)
    private int onDrawSelection(int x) {
        int i = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
        if (GameAnimation.INSTANCE.isEnabled() && GameAnimation.INSTANCE.getHotbar()) {
            float x0 = GameAnimation.INSTANCE.updateHotbar();
            return (int)(i - 91 - 1 + x0);
        }
        return i - 91 - 1 + client.player.getInventory().selectedSlot * 20;
    }

}

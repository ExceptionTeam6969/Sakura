package dev.exceptionteam.sakura.mixins.core.graphics;

import dev.exceptionteam.sakura.features.modules.impl.render.GameAnimation;
import dev.exceptionteam.sakura.features.modules.impl.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "renderItemHotbar", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V",
            ordinal = 1), index = 2)
    private int onDrawSelection(int x) {
        int i = minecraft.getWindow().getGuiScaledWidth() / 2;
        if (GameAnimation.INSTANCE.isEnabled() && GameAnimation.INSTANCE.getHotbar()) {
            float x0 = GameAnimation.INSTANCE.updateHotbar();
            return (int)(i - 91 - 1 + x0);
        }
        return i - 91 - 1 + minecraft.player.getInventory().selected * 20;
    }

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void onRenderStatusEffectOverlay(CallbackInfo info) {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.getPotionIcon()) info.cancel();
    }

}

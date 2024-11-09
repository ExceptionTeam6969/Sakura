package dev.exceptionteam.sakura.mixins.gui;

import dev.exceptionteam.sakura.features.modules.impl.render.GameAnimation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

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

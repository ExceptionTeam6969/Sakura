package dev.exceptionteam.sakura.mixins.graphics;

import dev.exceptionteam.sakura.features.modules.impl.render.GameAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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

}

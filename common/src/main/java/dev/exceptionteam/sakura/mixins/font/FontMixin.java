package dev.exceptionteam.sakura.mixins.font;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont;
import dev.exceptionteam.sakura.graphics.color.ColorRGB;
import dev.exceptionteam.sakura.graphics.font.FontRenderers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Font.class)
public class FontMixin {

    @Inject(method = "drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I", at = @At("HEAD"), cancellable = true)
    public void hookDrawInBatch(
            String text, float x, float y, int color,
            boolean dropShadow, Matrix4f pose, MultiBufferSource bufferSource,
            Font.DisplayMode displayMode, int backgroundColor,
            int packedLightCoords,
            CallbackInfoReturnable<Integer> cir
    ) {
        if (!CustomFont.INSTANCE.getOverrideMc()) return;

        GlStateManager._enableBlend();
        cir.setReturnValue((int) FontRenderers.INSTANCE.drawString(text, x, y, new ColorRGB(color), !dropShadow, 1.0f));
    }

}

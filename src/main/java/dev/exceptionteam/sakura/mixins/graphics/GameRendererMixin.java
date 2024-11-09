package dev.exceptionteam.sakura.mixins.graphics;

import com.llamalad7.mixinextras.sugar.Local;
import dev.exceptionteam.sakura.graphics.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.profiler.Profilers;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderWorld",
            at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void onRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci,
                              @Local(ordinal = 1) Matrix4f matrix4f2, @Local(ordinal = 1) float tickDelta,
                              @Local net.minecraft.client.util.math.MatrixStack matrixStack) {

        Profilers.get().push("sakura_render3d");

        RenderSystem.INSTANCE.onRender3d();

        Profilers.get().pop();

    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", shift = At.Shift.AFTER))
    public void onRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {

        Profilers.get().push("sakura_render2d");

        RenderSystem.INSTANCE.onRender2d();

        Profilers.get().pop();

    }

}

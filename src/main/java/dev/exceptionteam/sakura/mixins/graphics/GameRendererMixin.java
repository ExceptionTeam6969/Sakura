package dev.exceptionteam.sakura.mixins.graphics;

import com.llamalad7.mixinextras.sugar.Local;
import dev.exceptionteam.sakura.graphics.RenderSystem;
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.profiler.Profilers;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
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

        Matrix4fStack stack = com.mojang.blaze3d.systems.RenderSystem.getModelViewStack();

        stack.pushMatrix().mul(matrix4f2);

        stack.mul(MatrixStack.INSTANCE.peek().getPositionMatrix().invert());

        Profilers.get().push("sakura_render3d");

        RenderSystem.INSTANCE.updateMatrix();
        RenderSystem.INSTANCE.onRender3d();

        Profilers.get().pop();

        stack.popMatrix();
    }

}

package dev.exceptionteam.sakura.mixins.graphics;

import dev.exceptionteam.sakura.graphics.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.profiling.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderLevel",
            at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = {"ldc=hand"}))
    public void onRenderWorld(DeltaTracker deltaTracker, CallbackInfo ci) {

        Profiler.get().push("sakura_render3d");

        RenderSystem.INSTANCE.onRender3d();

        Profiler.get().pop();

    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V"))
    public void onRenderGui(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        Profiler.get().popPush("sakura-render2d");

        RenderSystem.INSTANCE.onRender2d();
    }

}

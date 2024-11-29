package dev.exceptionteam.sakura.mixins.graphics;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.exceptionteam.sakura.graphics.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderLevel",
            at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = {"ldc=hand"}), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void onRenderWorld(DeltaTracker deltaTracker, CallbackInfo ci, float f, ProfilerFiller profilerFiller,
                              boolean bl, Camera camera, Entity entity, float g, float h, Matrix4f matrix4f,
                              PoseStack poseStack, float i, float j, float n, Matrix4f matrix4f2,
                              Quaternionf quaternionf, Matrix4f matrix4f3) {

        Profiler.get().push("sakura_render3d");

        RenderSystem.INSTANCE.onRender3d();

        Profiler.get().pop();

    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"))
    public void onRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {

        Profiler.get().push("sakura_render2d");

        RenderSystem.INSTANCE.onRender2d();

        Profiler.get().pop();

    }

}

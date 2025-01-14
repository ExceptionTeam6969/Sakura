package dev.exceptionteam.sakura.mixins.graphics;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.exceptionteam.sakura.features.modules.impl.render.NoRender;
import dev.exceptionteam.sakura.graphics.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

    @Inject(method = "Lnet/minecraft/client/renderer/GameRenderer;bobHurt(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At("HEAD"), cancellable = true)
    private void bobHurt(PoseStack matrices, float tickDelta, CallbackInfo ci) {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.getNoHurtCam()) {
            ci.cancel();
        }
    }

    @Inject(method = "displayItemActivation", at = @At("HEAD"), cancellable = true)
    public void onDisplayItemActivation(ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() != Items.TOTEM_OF_UNDYING) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.getTotem()) {
            ci.cancel();
        }
    }

}

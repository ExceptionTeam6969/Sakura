package dev.exceptionteam.sakura.mixins.graphics;

import dev.exceptionteam.sakura.graphics.RenderSystem;
import dev.exceptionteam.sakura.graphics.matrix.MatrixEntry;
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.RotationAxis;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    public void onRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci) {
        RenderSystem.INSTANCE.onRender3d();

    }

}

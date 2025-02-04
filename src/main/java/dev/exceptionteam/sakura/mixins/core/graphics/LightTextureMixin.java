package dev.exceptionteam.sakura.mixins.core.graphics;

import dev.exceptionteam.sakura.features.modules.impl.render.FullBright;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @ModifyArgs(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;setPixelRGBA(III)V"))
    private void onUpdate(Args args) {
        if (FullBright.INSTANCE.isEnabled()) {
            args.set(2, 0xFFFFFFFF);
        }
    }

    @Inject(method = "getDarknessGamma", at = @At("HEAD"), cancellable = true)
    private void getDarknessFactor(float tickDelta, CallbackInfoReturnable<Float> info) {
        if (FullBright.INSTANCE.isEnabled()) {
            info.setReturnValue(0.0f);
        }
    }

}

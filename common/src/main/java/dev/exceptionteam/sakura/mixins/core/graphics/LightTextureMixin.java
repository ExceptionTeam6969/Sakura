package dev.exceptionteam.sakura.mixins.core.graphics;

import com.mojang.blaze3d.pipeline.TextureTarget;
import dev.exceptionteam.sakura.features.modules.impl.render.FullBright;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Shadow
    @Final
    private TextureTarget target;

    @Inject(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/TextureTarget;unbindWrite()V", shift = At.Shift.AFTER))
    private void onUpdate(CallbackInfo info) {
        if (FullBright.INSTANCE.isEnabled()) {
            this.target.clear();
        }
    }

    @Inject(method = "getDarknessGamma", at = @At("HEAD"), cancellable = true)
    private void getDarknessFactor(float tickDelta, CallbackInfoReturnable<Float> info) {
        if (FullBright.INSTANCE.isEnabled()) {
            info.setReturnValue(0.0f);
        }
    }

}

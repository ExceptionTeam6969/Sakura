package dev.exceptionteam.sakura.mixins.graphics;

import dev.exceptionteam.sakura.features.modules.impl.render.FullBright;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "getSkyLightLevel", at = @At("RETURN"), cancellable = true)
    private void onGetSkyLight(CallbackInfoReturnable<Integer> info) {
        if (FullBright.INSTANCE.isEnabled()) {
            info.setReturnValue(FullBright.INSTANCE.getBrightness());
        }
    }

}

package dev.exceptionteam.sakura.mixins.graphics;

import dev.exceptionteam.sakura.features.modules.impl.render.FullBright;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @ModifyVariable(method = "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I", at = @At(value = "STORE"), ordinal = 0)
    private static int getLightColor(int sky) {
        if (FullBright.INSTANCE.isEnabled()) {
            return FullBright.INSTANCE.getBrightness();
        }
        return sky;
    }

}

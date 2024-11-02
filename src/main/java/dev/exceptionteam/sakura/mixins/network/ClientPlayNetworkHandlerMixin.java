package dev.exceptionteam.sakura.mixins.network;

import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onExplosion", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onExplosionVelocity(ExplosionS2CPacket packet, CallbackInfo ci) {
        if (Velocity.INSTANCE.isEnabled()) {
            if (Velocity.INSTANCE.getShouldCancelExplosion()) ci.cancel();
        }
    }

}

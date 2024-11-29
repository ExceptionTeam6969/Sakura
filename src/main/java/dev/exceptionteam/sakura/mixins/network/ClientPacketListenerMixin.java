package dev.exceptionteam.sakura.mixins.network;

import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleExplosion", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onExplosionVelocity(ClientboundExplodePacket clientboundExplodePacket, CallbackInfo ci) {
        if (Velocity.INSTANCE.isEnabled()) {
            if (Velocity.INSTANCE.getShouldCancelExplosion()) ci.cancel();
        }
    }

}

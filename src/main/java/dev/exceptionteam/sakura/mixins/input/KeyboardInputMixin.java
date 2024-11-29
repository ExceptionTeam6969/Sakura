package dev.exceptionteam.sakura.mixins.input;

import dev.exceptionteam.sakura.events.impl.MovementInputEvent;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends ClientInput {

    @Inject(method = "tick", at = @At("RETURN"))
    public void onTick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        MovementInputEvent event = new MovementInputEvent(forwardImpulse, leftImpulse);
        event.post();

        forwardImpulse = event.getForward();
        leftImpulse = event.getStrafe();
    }

}

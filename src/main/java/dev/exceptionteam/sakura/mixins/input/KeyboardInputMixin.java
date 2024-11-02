package dev.exceptionteam.sakura.mixins.input;

import dev.exceptionteam.sakura.events.impl.MovementInputEvent;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    public void onTick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        MovementInputEvent event = new MovementInputEvent(movementForward, movementSideways);
        event.post();

        movementForward = event.getForward();
        movementSideways = event.getStrafe();
    }

}

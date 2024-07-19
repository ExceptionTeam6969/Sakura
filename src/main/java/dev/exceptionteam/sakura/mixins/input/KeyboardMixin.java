package dev.exceptionteam.sakura.mixins.input;

import dev.exceptionteam.sakura.events.impl.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Mutable
    @Final
    @Shadow
    private MinecraftClient client;

    public KeyboardMixin(MinecraftClient client) {
        this.client = client;
    }

    @Inject(method = "onKey", at = @At(value = "HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {

        KeyEvent event = new KeyEvent(key, action);
        event.post();
        if (event.isCancelled()) info.cancel();

    }

}

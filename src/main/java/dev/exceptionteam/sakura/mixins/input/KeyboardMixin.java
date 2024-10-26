package dev.exceptionteam.sakura.mixins.input;

import dev.exceptionteam.sakura.events.impl.KeyEvent;
import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen;
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
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

        if (client.currentScreen != null && !(client.currentScreen instanceof ClickGUIScreen
                || client.currentScreen instanceof HUDEditorScreen)) return;
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;

        KeyEvent event = new KeyEvent(key, action);
        event.post();
        if (event.isCancelled()) info.cancel();

    }

}

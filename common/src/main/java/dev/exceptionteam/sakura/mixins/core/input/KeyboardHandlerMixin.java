package dev.exceptionteam.sakura.mixins.core.input;

import dev.exceptionteam.sakura.events.impl.KeyEvent;
import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen;
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Mutable
    @Final
    @Shadow
    private Minecraft minecraft;

    public KeyboardHandlerMixin(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Inject(method = "keyPress", at = @At(value = "HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {

        if (minecraft.screen != null && !(minecraft.screen instanceof ClickGUIScreen
                || minecraft.screen instanceof HUDEditorScreen)) return;
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;

        KeyEvent event = new KeyEvent(key, action);
        event.post();
        if (event.isCancelled()) info.cancel();

    }

}

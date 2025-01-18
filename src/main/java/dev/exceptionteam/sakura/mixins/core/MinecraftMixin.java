package dev.exceptionteam.sakura.mixins.core;

import dev.exceptionteam.sakura.Sakura;
import dev.exceptionteam.sakura.events.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent;beginStep(Lnet/minecraft/client/telemetry/TelemetryProperty;)V"))
    public void onInit(GameConfig gameConfig, CallbackInfo ci) {
        Sakura.INSTANCE.init();
    }

    @Inject(method = "setLevel", at = @At("RETURN"))
    public void joinLevel(ClientLevel clientLevel, ReceivingLevelScreen.Reason reason, CallbackInfo ci) {
        new JoinLevelEvent().post();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("RETURN"))
    public void disconnect(Screen screen, CallbackInfo ci) {
        new DisconnectEvent().post();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTickPre(CallbackInfo ci) {
        TickEvent.Pre.INSTANCE.post();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;tick()V"))
    public void onTickUpdate(CallbackInfo ci) {
        TickEvent.Update.INSTANCE.post();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTickPost(CallbackInfo ci) {
        TickEvent.Post.INSTANCE.post();
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/metrics/profiling/MetricsRecorder;endTick()V"))
    public void gameLoopAfterRender(CallbackInfo ci) {
        GameLoopEvent.AfterRender.INSTANCE.post();
    }

    @Inject(method = "run", at = @At(value = "RETURN"))
    public void shutdown(CallbackInfo ci) {
        QuitGameEvent.INSTANCE.post();
    }

    @Inject(method = "resizeDisplay", at = @At("TAIL"))
    private void onWindowResize(CallbackInfo info) {
        new WindowResizeEvent().post();
    }

}

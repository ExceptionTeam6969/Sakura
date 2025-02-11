package dev.exceptionteam.sakura.mixins.core.entity;

import dev.exceptionteam.sakura.events.impl.PlayerMotionEvent;
import dev.exceptionteam.sakura.events.impl.PlayerUpdateEvents;
import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends EntityMixin {

    @Unique
    public PlayerMotionEvent motionEvent;

    @Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true)
    public void pushOutOfBlocksHook(double x, double z, CallbackInfo ci) {
        if (Velocity.INSTANCE.isEnabled() && Velocity.INSTANCE.getNoPush()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
    private void onTickMovementHead(CallbackInfo callbackInfo) {
        new PlayerMotionEvent.Pre().post();
        motionEvent = new PlayerMotionEvent(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot(), this.onGround());
        motionEvent.post();
        if (motionEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "sendPosition", at = @At("RETURN"))
    private void onTickMovementRet(CallbackInfo callbackInfo) {
        new PlayerMotionEvent.Post().post();
    }

    @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"))
    private double posXHook(LocalPlayer instance) {
        return motionEvent.getX();
    }

    @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"))
    private double posYHook(LocalPlayer instance) {
        return motionEvent.getY();
    }

    @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"))
    private double posZHook(LocalPlayer instance) {
        return motionEvent.getZ();
    }

    @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
    private float pitchHook(LocalPlayer instance) {
        return motionEvent.getPitch();
    }

    @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
    private float yawHook(LocalPlayer instance) {
        return motionEvent.getYaw();
    }

    @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z"))
    private boolean groundHook(LocalPlayer instance) {
        return motionEvent.isOnGround();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickHead(CallbackInfo ci) {
        PlayerUpdateEvents.Pre.INSTANCE.post();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTickReturn(CallbackInfo ci) {
        PlayerUpdateEvents.Post.INSTANCE.post();
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onAiStepHead(CallbackInfo ci) {
        PlayerUpdateEvents.AiStepPre.INSTANCE.post();
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void onAiStepReturn(CallbackInfo ci) {
        PlayerUpdateEvents.AiStepPost.INSTANCE.post();
    }

    @Inject(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;noPhysics:Z"))
    private void onAiStepNoPhysics(CallbackInfo ci) {
        PlayerUpdateEvents.AiStepUpdate.INSTANCE.post();
    }

}

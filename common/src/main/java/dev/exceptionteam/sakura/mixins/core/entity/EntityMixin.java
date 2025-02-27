package dev.exceptionteam.sakura.mixins.core.entity;

import dev.exceptionteam.sakura.events.impl.PlayerVelocityStrafeEvents;
import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Vec3 position();

    @Shadow
    public abstract boolean onGround();

    @Shadow
    public abstract float getXRot();

    @Shadow
    public abstract float getYRot();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow public abstract int getId();

    @Redirect(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPushedByFluid()Z"))
    public boolean updateMovementInFluid(Entity entity) {
        return entity.isPushedByFluid() && !(Velocity.INSTANCE.isEnabled() && Velocity.INSTANCE.getNoPush());
    }

    @Inject(method = "moveRelative", at = @At(value = "HEAD"))
    private void onMovePre(float amount, Vec3 relative, CallbackInfo ci) {
        PlayerVelocityStrafeEvents.Pre.INSTANCE.post();
    }

    @Inject(method = "moveRelative", at = @At(value = "RETURN"))
    private void onMovePost(float amount, Vec3 relative, CallbackInfo ci) {
        PlayerVelocityStrafeEvents.Post.INSTANCE.post();
    }

}

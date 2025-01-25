package dev.exceptionteam.sakura.mixins.core.entity;

import dev.exceptionteam.sakura.events.impl.PlayerVelocityStrafeEvent;
import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @Shadow
    protected static Vec3 getInputVector(Vec3 relative, float motionScaler, float facing) {
        return null;
    }

    @Shadow public abstract float getYRot(float f);

    @Shadow public abstract int getId();

    @Redirect(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPushedByFluid()Z"))
    public boolean updateMovementInFluid(Entity entity) {
        return entity.isPushedByFluid() && !(Velocity.INSTANCE.isEnabled() && Velocity.INSTANCE.getNoPush());
    }

    @Redirect(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getInputVector(Lnet/minecraft/world/phys/Vec3;FF)Lnet/minecraft/world/phys/Vec3;"))
    public Vec3 hookVelocity(Vec3 movementInput, float speed, float yaw) {
        if (Minecraft.getInstance().player == null) return getInputVector(movementInput, speed, yaw);

        if (this.getId() == Minecraft.getInstance().player.getId()) {
            PlayerVelocityStrafeEvent event = new PlayerVelocityStrafeEvent(yaw);
            event.post();
            return getInputVector(movementInput, speed, event.getYaw());
        }

        return getInputVector(movementInput, speed, yaw);
    }

}

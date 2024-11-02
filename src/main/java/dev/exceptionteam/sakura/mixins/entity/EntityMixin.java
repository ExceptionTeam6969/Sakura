package dev.exceptionteam.sakura.mixins.entity;

import dev.exceptionteam.sakura.events.impl.PlayerVelocityStrafeEvent;
import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract boolean isOnGround();

    @Shadow
    public abstract float getPitch();

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    protected static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

    @Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByFluids()Z"))
    public boolean updateMovementInFluid(Entity entity) {
        return entity.isPushedByFluids() && !(Velocity.INSTANCE.isEnabled() && Velocity.INSTANCE.getNoPush());
    }

    @Redirect(method = "updateVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"))
    public Vec3d hookVelocity(Vec3d movementInput, float speed, float yaw) {
        if ((Object) this == MinecraftClient.getInstance().player) {
            PlayerVelocityStrafeEvent event = new PlayerVelocityStrafeEvent(yaw);
            event.post();
            return movementInputToVelocity(movementInput, speed, event.getYaw());
        }

        return movementInputToVelocity(movementInput, speed, yaw);
    }

}

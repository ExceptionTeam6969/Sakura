package dev.exceptionteam.sakura.mixins.entity;

import dev.exceptionteam.sakura.features.modules.impl.movement.Velocity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {

    @Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByFluids()Z"))
    public boolean updateMovementInFluid(Entity entity) {
        return entity.isPushedByFluids() && !(Velocity.INSTANCE.isEnabled() && Velocity.INSTANCE.getNoPush());
    }

}

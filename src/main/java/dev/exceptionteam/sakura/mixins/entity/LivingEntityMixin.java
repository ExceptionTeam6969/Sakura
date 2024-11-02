package dev.exceptionteam.sakura.mixins.entity;

import dev.exceptionteam.sakura.events.impl.PlayerJumpEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float hookYaw(LivingEntity instance) {
        if (instance == MinecraftClient.getInstance().player) {
            PlayerJumpEvent event = new PlayerJumpEvent(instance.getYaw());
            event.post();
            return event.getYaw();
        }
        return instance.getYaw();
    }

}

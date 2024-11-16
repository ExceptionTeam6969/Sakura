package dev.exceptionteam.sakura.mixins.entity;

import dev.exceptionteam.sakura.events.impl.PlayerJumpEvent;
import dev.exceptionteam.sakura.features.modules.impl.player.CancelUsing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow protected ItemStack activeItemStack;

    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float hookYaw(LivingEntity instance) {
        if (instance == MinecraftClient.getInstance().player) {
            PlayerJumpEvent event = new PlayerJumpEvent(instance.getYaw());
            event.post();
            return event.getYaw();
        }
        return instance.getYaw();
    }

    @Inject(method = "tickActiveItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickItemStackUsage(Lnet/minecraft/item/ItemStack;)V"))
    public void onActiveItemStackUpdate(CallbackInfo ci) {
        if (CancelUsing.INSTANCE.isDisabled()) return;

        if (CancelUsing.INSTANCE.getShield() && activeItemStack.getItem() instanceof ShieldItem) {
            activeItemStack = ItemStack.EMPTY;
        }
    }

}

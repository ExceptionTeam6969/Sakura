package dev.exceptionteam.sakura.mixins.core.entity;

import dev.exceptionteam.sakura.events.impl.PlayerJumpEvent;
import dev.exceptionteam.sakura.features.modules.impl.player.CancelUsing;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    protected ItemStack useItem;

    @Redirect(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
    private float hookYaw(LivingEntity instance) {
        if (instance == Minecraft.getInstance().player) {
            PlayerJumpEvent event = new PlayerJumpEvent(instance.getYRot());
            event.post();
            return event.getYaw();
        }
        return instance.getYRot();
    }

    @Inject(method = "updatingUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateUsingItem(Lnet/minecraft/world/item/ItemStack;)V"))
    public void onActiveItemStackUpdate(CallbackInfo ci) {
        if (CancelUsing.INSTANCE.isDisabled()) return;

        if (CancelUsing.INSTANCE.getShield() && useItem.getItem() instanceof ShieldItem) {
            useItem = ItemStack.EMPTY;
        }
    }

}

package dev.exceptionteam.sakura.mixins.core.entity;

import dev.exceptionteam.sakura.events.impl.PlayerJumpEvents;
import dev.exceptionteam.sakura.features.modules.impl.player.CancelUsing;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    protected ItemStack useItem;

    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"))
    private void onJumpPre(CallbackInfo ci) {
        PlayerJumpEvents.Pre.INSTANCE.post();
    }

    @Inject(method = "jumpFromGround", at = @At(value = "RETURN"))
    private void onJumpPost(CallbackInfo ci) {
        PlayerJumpEvents.Post.INSTANCE.post();
    }

    @Inject(method = "updatingUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateUsingItem(Lnet/minecraft/world/item/ItemStack;)V"))
    public void onActiveItemStackUpdate(CallbackInfo ci) {
        if (CancelUsing.INSTANCE.isDisabled()) return;

        if (CancelUsing.INSTANCE.getShield() && useItem.getItem() instanceof ShieldItem) {
            useItem = ItemStack.EMPTY;
        }
    }

}

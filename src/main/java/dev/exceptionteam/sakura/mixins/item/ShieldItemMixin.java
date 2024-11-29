package dev.exceptionteam.sakura.mixins.item;

import dev.exceptionteam.sakura.features.modules.impl.player.CancelUsing;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void onUse(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (CancelUsing.INSTANCE.isEnabled() && CancelUsing.INSTANCE.getShield()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

}

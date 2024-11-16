package dev.exceptionteam.sakura.mixins.item;

import dev.exceptionteam.sakura.features.modules.impl.player.CancelUsing;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (CancelUsing.INSTANCE.isEnabled() && CancelUsing.INSTANCE.getShield()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

}

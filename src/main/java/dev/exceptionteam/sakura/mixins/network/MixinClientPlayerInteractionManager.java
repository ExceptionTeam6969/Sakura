package dev.exceptionteam.sakura.mixins.network;



import dev.exceptionteam.sakura.events.impl.PlayerDamageBlockEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MixinClientPlayerInteractionManager {
	@Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
	private void onAttackBlock(BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
		PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(pos,facing);
		event.post();

		if (isCancelled()) {
			cir.setReturnValue(false);
		}
	}
}

package dev.exceptionteam.sakura.mixins.core.network;


import dev.exceptionteam.sakura.events.impl.PlayerDamageBlockEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MixinClientPlayerInteractionManager {

	@Inject(method = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
	private void onAttackBlock(BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
		PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(pos,facing);
		event.post();

		if (event.isCancelled()) {
			cir.setReturnValue(false);
		}
	}
}

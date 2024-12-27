package dev.exceptionteam.sakura.mixins.graphics;

import dev.exceptionteam.sakura.events.impl.AddParticleEvent;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void onAdd(Particle effect, CallbackInfo ci) {
        AddParticleEvent event = new AddParticleEvent(effect);
        event.post();
        if(event.isCancelled()) {
            ci.cancel();
        }
    }

}

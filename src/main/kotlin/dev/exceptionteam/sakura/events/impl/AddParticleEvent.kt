package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.CancellableEvent
import net.minecraft.client.particle.Particle

class AddParticleEvent(
    val particle: Particle
): CancellableEvent()
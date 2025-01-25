package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.events.impl.AddParticleEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import net.minecraft.client.particle.HugeExplosionParticle

object NoRender: Module(
    name = "no-render",
    category = Category.RENDER,
) {
    private val explosions by setting("explosions", true)
    val fire by setting("fire", true)
    val totem by setting("totem", false)
    val underWater by setting("under-water", true)
    val inWall by setting("in-wall", true)
    val noHurtCam by setting("no-hurt-cam", true)
    val potionIcon by setting("potion-icon", true)
    
    init {

        listener<AddParticleEvent> { e ->
            when (e.particle) {
                is HugeExplosionParticle -> if (explosions) e.cancel()
                else -> {}

            }

        }
    }
}
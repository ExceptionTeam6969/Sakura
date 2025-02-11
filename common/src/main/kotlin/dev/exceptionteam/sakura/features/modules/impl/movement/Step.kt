package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.Wrapper
import net.minecraft.world.entity.ai.attributes.Attributes

object Step: Module(
    name = "step",
    category = Category.MOVEMENT
) {

    private val height by setting("height", 2.0, 1.0..5.0)

    private var prevStepHeight = 0.0

    override fun hudInfo(): String {
        return "Vanilla"
    }

    init {
        onEnable {
            val player = Wrapper.player ?: return@onEnable
            val attribute = player.getAttribute(Attributes.STEP_HEIGHT) ?: return@onEnable
            prevStepHeight = attribute.baseValue
        }

        onDisable {
            val player = Wrapper.player ?: return@onDisable
            val attribute = player.getAttribute(Attributes.STEP_HEIGHT) ?: return@onDisable
            attribute.baseValue = prevStepHeight
        }

        nonNullListener<TickEvents.Update> {
            val attribute = player.getAttribute(Attributes.STEP_HEIGHT) ?: return@nonNullListener
            attribute.baseValue = height
        }

    }


}
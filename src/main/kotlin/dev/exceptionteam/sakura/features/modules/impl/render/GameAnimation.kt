package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.easing.AnimationFlag
import dev.exceptionteam.sakura.graphics.easing.Easing
import dev.exceptionteam.sakura.utils.threads.runSafe

object GameAnimation: Module(
    name = "game-animation",
    category = Category.RENDER
) {

    private var hotbarAnimation = AnimationFlag(Easing.OUT_CUBIC, 200.0f)
    var hotbar by setting("hotbar", true)

    init {
        onEnable {
            runSafe {
                val currentPos = player.inventory.selectedSlot * 20.0f
                hotbarAnimation.forceUpdate(currentPos, currentPos)
            }
        }
    }

    fun updateHotbar(): Float {
        return runSafe {
            val currentPos = player.inventory.selectedSlot * 20f
            return hotbarAnimation.getAndUpdate(currentPos)
        } ?: 0f
    }

}
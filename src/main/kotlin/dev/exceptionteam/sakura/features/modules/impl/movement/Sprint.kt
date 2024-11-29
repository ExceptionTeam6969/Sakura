package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.player.PlayerUtils.isMoving
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

object Sprint: Module(
    name = "sprint",
    category = Category.MOVEMENT,
) {

    private val mode by setting("mode", Mode.LEGIT)

    init {

        nonNullListener<TickEvent.Post> {
            when (mode) {
                Mode.LEGIT -> {
                    mc.options.keySprint.isDown = true
                }
                Mode.RAGE -> {
                    if (isMoving()) player.isSprinting = true
                }
            }
        }

    }

    private enum class Mode(override val key: CharSequence): TranslationEnum {
        LEGIT("legit"),
        RAGE("rage"),
    }

}
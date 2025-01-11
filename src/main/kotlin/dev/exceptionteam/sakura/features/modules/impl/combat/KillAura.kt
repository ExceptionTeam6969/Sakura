package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTarget
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTargetPlayer
import dev.exceptionteam.sakura.utils.player.InteractionUtils.attack
import dev.exceptionteam.sakura.utils.timing.TimerUtils

/**
 * @author LangYa
 * @since 2024/12/14 18:09
 */
object KillAura: Module(
    name = "kill-aura",
    category = Category.COMBAT
) {
    private val range by setting("range", 3.0f, 2.5f..6.0f)
    private val delay by setting("delay", 500, 50..3000)
    private val onlyPlayers by setting("only-players", true)
    private val rotation by setting("rotation", true)
    private val swing by setting("swing", true)

    private val timer = TimerUtils().apply { reset() }

    init {
        nonNullListener<TickEvent.Update> {
            if (!timer.passedAndReset(delay)) return@nonNullListener

            if (onlyPlayers) getTargetPlayer(range)?.let {
                attack(it, rotation, swing)
            } else getTarget(range)?.let {
                attack(it, rotation, swing)
            }
        }
    }

}

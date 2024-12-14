package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.utils.math.RotationUtils.getRotationTo
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.sq
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity

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
    private val rotation by setting("rotation", true)
    private val swing by setting("swing", true)

    private val timer = TimerUtils().apply { reset() }

    init {
        nonNullListener<TickEvent.Update> {
            if (!timer.passedAndReset(delay)) return@nonNullListener

            for (ent in world.entitiesForRendering()) {
                if (ent.id == player.id) continue
                if (ent.distanceSqTo(player) >= range.sq) continue

                attack(ent)
                break
            }
        }
    }

    private fun NonNullContext.attack(target: Entity) {
        val rotAngle = getRotationTo(target.position())
        addRotation(rotAngle, 0, rotation) {
            connection.send(ServerboundInteractPacket.createAttackPacket(target, true))
            if (swing) player.swing(InteractionHand.MAIN_HAND)
        }
    }
}

package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PlayerMotionEvent
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener

object RotationManager {

    init {
        nonNullListener<TickEvent.Pre> {
            rotationInfo?.func?.invoke()
            rotationInfo = null
        }

        nonNullListener<PlayerMotionEvent> { e ->
            rotationInfo?.let {
                e.yaw = it.yaw
                e.pitch = it.pitch
            }
        }
    }

    var rotationInfo: RotationInfo? = null; private set

    fun NonNullContext.addRotation(yaw: Float, pitch: Float, priority: Int, func: () -> Unit) {
        RotationManager.addRotation(yaw, pitch, priority, func)
    }

    fun addRotation(yaw: Float, pitch: Float, priority: Int, func: () -> Unit) {
        if (rotationInfo == null) {
            rotationInfo = RotationInfo(yaw, pitch, priority, func)
        } else {
            if (priority > rotationInfo!!.priority) {
                rotationInfo = RotationInfo(yaw, pitch, priority, func)
            }
        }
    }

    data class RotationInfo(
        val yaw: Float,
        val pitch: Float,
        val priority: Int,
        val func: () -> Unit
    )

}
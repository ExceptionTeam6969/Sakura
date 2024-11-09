package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PlayerMotionEvent
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.utils.math.vector.Vec2f

object RotationManager {

    init {
        nonNullListener<TickEvent.Pre>(alwaysListening = true) {
            rotationInfo = null
        }

        nonNullListener<PlayerMotionEvent>(alwaysListening = true, priority = Int.MAX_VALUE) { e ->
            rotationInfo?.let {
                e.yaw = it.yaw
                e.pitch = it.pitch
            }
        }
    }

    var rotationInfo: RotationInfo? = null; private set

    /**
     * Add a rotation to the rotation manager.
     */
    fun NonNullContext.addRotation(
        yaw: Float, pitch: Float, priority: Int
    ) = RotationManager.addRotation(yaw, pitch, priority)

    fun NonNullContext.addRotation(
        rotation: Vec2f, priority: Int
    ) = RotationManager.addRotation(rotation.x, rotation.y, priority)

    fun addRotation(yaw: Float, pitch: Float, priority: Int) {
        if (rotationInfo == null) {
            rotationInfo = RotationInfo(yaw, pitch, priority)
        } else {
            if (priority > rotationInfo!!.priority) {
                rotationInfo = RotationInfo(yaw, pitch, priority)
            }
        }
    }

    data class RotationInfo(
        val yaw: Float,
        val pitch: Float,
        val priority: Int
    )

}
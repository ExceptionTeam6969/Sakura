package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.CancellableEvent
import dev.exceptionteam.sakura.events.Event

class PlayerMotionEvent(
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Float,
    var pitch: Float,
    var isOnGround: Boolean
): CancellableEvent() {

    class Pre: Event()
    class Post: Event()

}
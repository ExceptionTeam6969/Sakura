package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event

class MovementInputEvent(
    var forward: Float,
    var strafe: Float,
): Event() {
    object Pre: Event()
}
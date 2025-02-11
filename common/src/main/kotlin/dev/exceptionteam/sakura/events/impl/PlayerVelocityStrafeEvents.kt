package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event

sealed class PlayerVelocityStrafeEvents {
    object Pre: Event()

    object Post: Event()
}
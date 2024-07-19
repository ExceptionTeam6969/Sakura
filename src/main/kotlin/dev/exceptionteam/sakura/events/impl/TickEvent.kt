package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event

sealed class TickEvent {

    object Pre: Event()

    object Post: Event()

}
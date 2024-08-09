package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event

sealed class GameLoopEvent {

    object AfterRender: Event()

}
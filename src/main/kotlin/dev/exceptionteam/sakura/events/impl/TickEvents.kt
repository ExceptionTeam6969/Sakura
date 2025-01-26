package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event

sealed class TickEvents {

    object Pre: Event()

    /*
     * This event is called after the packet received
     */
    object Update: Event()

    object Post: Event()

}
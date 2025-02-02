package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event

sealed class PlayerUpdateEvents {

    object Pre: Event()

    object Post: Event()

    object AiStepPre: Event()

    object AiStepPost: Event()

    object AiStepUpdate: Event()

}
package dev.exceptionteam.sakura.managers

import dev.exceptionteam.sakura.events.SafeClientEvent
import dev.exceptionteam.sakura.managers.impl.*

object Managers {

    fun init() {
        SafeClientEvent

        GraphicsManager.onInit()
        ModuleManager.onInit()
    }

}
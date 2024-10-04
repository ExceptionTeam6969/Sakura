package dev.exceptionteam.sakura.managers

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.managers.impl.*

object Managers {

    fun init() {
        NonNullContext

        TranslationManager.onInit()
        GraphicsManager.onInit()
        ModuleManager.onInit()
    }

}
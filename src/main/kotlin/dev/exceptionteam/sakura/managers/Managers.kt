package dev.exceptionteam.sakura.managers

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.managers.impl.*

object Managers {

    init {
        NonNullContext

        TranslationManager
        GraphicsManager
        ModuleManager
        ConfigManager
        FriendManager
        CommandManager
    }

}
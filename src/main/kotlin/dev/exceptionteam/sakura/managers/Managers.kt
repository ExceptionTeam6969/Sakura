package dev.exceptionteam.sakura.managers

import dev.exceptionteam.sakura.addons.AddonManager
import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.managers.impl.*

object Managers {

    init {
        NonNullContext

        AddonManager
        TranslationManager
        GraphicsManager
        ModuleManager
        ConfigManager
        FriendManager
        CommandManager
        RotationManager
        HotbarManager
        TargetManager
    }

}
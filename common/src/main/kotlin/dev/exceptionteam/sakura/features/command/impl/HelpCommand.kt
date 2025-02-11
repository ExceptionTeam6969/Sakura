package dev.exceptionteam.sakura.features.command.impl

import dev.exceptionteam.sakura.features.command.Command
import dev.exceptionteam.sakura.features.command.executor
import dev.exceptionteam.sakura.managers.impl.CommandManager
import dev.exceptionteam.sakura.utils.ingame.ChatUtils

object HelpCommand: Command("help", description = "Print commands description and usage.") {

    init {
        executor {
            val helpMessage =CommandManager.getHelpMessage()
            ChatUtils.sendMessage(helpMessage)
        }
    }

}
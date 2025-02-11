package dev.exceptionteam.sakura.features.command.impl

import dev.exceptionteam.sakura.features.command.Command
import dev.exceptionteam.sakura.features.command.executor
import dev.exceptionteam.sakura.features.command.key
import dev.exceptionteam.sakura.features.command.literal
import dev.exceptionteam.sakura.features.command.module
import dev.exceptionteam.sakura.utils.ingame.ChatUtils

object BindCommand : Command("bind", description = "Bind module to key. example: /bind click-gui RIGHT_SHIFT") {
    init {
        literal {
            module { moduleArgument ->
                key { keyArgument ->
                    executor {
                        val module = moduleArgument.value()

                        module.keyBind.keyCode = keyArgument.value()
                        ChatUtils.sendMessage("Bind ${module.name.translation} to ${keyArgument.originValue()}")
                    }
                }
            }
        }
    }
}
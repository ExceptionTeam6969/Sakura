package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.command.Command
import dev.exceptionteam.sakura.features.command.impl.*
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket

object CommandManager {
    private val commands = mutableListOf<Command>()

    var commandPrefix = "&"

    init {
        nonNullListener<PacketEvents.Send>(priority = Int.MAX_VALUE, alwaysListening = true) { e ->
            if (e.packet is ChatMessageC2SPacket) {
                if (!e.packet.chatMessage.startsWith(commandPrefix)) return@nonNullListener
                e.cancel()
                val input = e.packet.chatMessage.removePrefix(commandPrefix)

                invoke(input)?.let {
                    ChatUtils.sendMessage(it)
                }
            }
        }

        addCommand(HelpCommand)
        addCommand(BindCommand)
        addCommand(FriendCommand)
    }

    fun addCommand(command: Command) {
        commands.add(command)
    }

    // TODO: Complete is not finished yet
    fun complete(args: List<String>): List<String> {
        if (args.isEmpty()) {
            return emptyList()
        }

        return commands.flatMap { it.complete(args) }
    }

    fun invoke(input: String): String? {
        return runCatching {
            commands.forEach { it.invoke(input.removePrefix(commandPrefix)) }
        }.exceptionOrNull()?.message
    }

    fun getHelpMessage(): String {
        val builder = StringBuilder()
        builder.appendLine("Commands:")
        builder.appendLine("----- Divider -----")
        commands.forEach {
            builder.appendLine(" Command: ${it.name}")
            if (it.alias.isNotEmpty()) {
                builder.appendLine(" Alias: ${it.alias.joinToString(" ")}")
            }
            builder.appendLine(" Description: ${it.description}")
            builder.appendLine(" Usage:")

            val lines = it.getArgumentTreeString().lines()
            val usage = lines
                .filter { line -> line.isNotEmpty() }
                .joinToString("\n") {
                    " - $commandPrefix$it"
                }

            builder.appendLine(usage)
            builder.appendLine("----- Divider -----")
        }
        return builder.toString()
    }
}
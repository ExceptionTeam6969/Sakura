package dev.exceptionteam.sakura.features.command

import dev.exceptionteam.sakura.features.command.argument.Argument
import dev.exceptionteam.sakura.features.command.argument.ArgumentTree
import dev.exceptionteam.sakura.features.command.argument.impl.StringArgument

abstract class Command(
    val name: String,
    val alias: Array<out String> = emptyArray(),
    val description: String = "Empty"
) : CommandBuilder {
    override val index: Int = 0
    private val rootArgumentTree = ArgumentTree(StringArgument(0, name, alias, true))

    fun complete(args: List<String>): List<String> {
        return rootArgumentTree.complete(args)
    }

    fun invoke(input: String) {
        if (input.isEmpty()) {
            return
        }

        rootArgumentTree.invoke(input)
    }

    fun getArgumentTreeString(): String {
        return rootArgumentTree.getArgumentTreeString()
    }

    override fun <T : Argument<*>> addArgument(argument: T, block: CommandBuilder.(T) -> Unit) {
        val argumentTree = ArgumentTree(argument)
        DefaultCommandBuilder(argument.index, argumentTree).block(argument)
        rootArgumentTree.appendArgument(argumentTree)
    }
}
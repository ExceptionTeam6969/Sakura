package dev.exceptionteam.sakura.features.command

import dev.exceptionteam.sakura.features.command.argument.Argument
import dev.exceptionteam.sakura.features.command.argument.ArgumentTree

class DefaultCommandBuilder(
    override val index: Int,
    private val prevArgumentTree: ArgumentTree
) : CommandBuilder {
    override fun <T : Argument<*>> addArgument(argument: T, block: CommandBuilder.(T) -> Unit) {
        val argumentTree = ArgumentTree(argument)
        DefaultCommandBuilder(argument.index, argumentTree).block(argument)
        prevArgumentTree.appendArgument(argumentTree)
    }
}
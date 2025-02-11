package dev.exceptionteam.sakura.features.command

import dev.exceptionteam.sakura.features.command.argument.Argument
import dev.exceptionteam.sakura.features.command.argument.impl.AnyArgument
import dev.exceptionteam.sakura.features.command.argument.impl.CommandExecutor
import dev.exceptionteam.sakura.features.command.argument.impl.ExecutorArgument
import dev.exceptionteam.sakura.features.command.argument.impl.FriendArgument
import dev.exceptionteam.sakura.features.command.argument.impl.KeyArgument
import dev.exceptionteam.sakura.features.command.argument.impl.ModuleArgument
import dev.exceptionteam.sakura.features.command.argument.impl.StringArgument

interface CommandBuilder {
    val index: Int

    fun <T : Argument<*>> addArgument(argument: T, block: CommandBuilder.(T) -> Unit)
}

fun <T : CommandBuilder> T.literal(block: CommandBuilder.() -> Unit) {
    this.block()
}

fun <T : CommandBuilder> T.key(block: CommandBuilder.(KeyArgument) -> Unit) {
    addArgument(KeyArgument(index + 1), block)
}

fun <T : CommandBuilder> T.friend(block: CommandBuilder.(FriendArgument) -> Unit) {
    addArgument(FriendArgument(index + 1), block)
}

fun <T : CommandBuilder> T.any(block: CommandBuilder.(AnyArgument) -> Unit) {
    addArgument(AnyArgument(index + 1), block)
}

fun <T : CommandBuilder> T.module(block: CommandBuilder.(ModuleArgument) -> Unit) {
    addArgument(ModuleArgument(index + 1), block)
}

fun <T : CommandBuilder> T.match(
    string: String,
    alias: Array<String> = emptyArray(),
    ignoreCase: Boolean = false,
    block: CommandBuilder.() -> Unit
) {
    addArgument(StringArgument(index + 1, string, alias, ignoreCase)) { block() }
}

fun <T : CommandBuilder> T.executor(description: String = "Empty", block: CommandExecutor.() -> Unit) {
    val executorArgument = ExecutorArgument(index + 1, description, block)
    addArgument(executorArgument) {}
}
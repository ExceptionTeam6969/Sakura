package dev.exceptionteam.sakura.features.command.argument.impl

import dev.exceptionteam.sakura.features.command.argument.Argument

class CommandExecutor(
    private val args: List<String>,
    private val block: CommandExecutor.() -> Unit
) {
    fun <T> Argument<T>.value(): T {
        return this.convertToType(args[this.index]) ?: throw Exception("Convert type error!")
    }

    fun Argument<*>.originValue(): String {
        return args[this.index]
    }

    fun invoke() {
        block()
    }
}

class ExecutorArgument(
    index: Int,
    private val description: String,
    private val block: CommandExecutor.() -> Unit
) : Argument<Unit>(index) {
    override fun complete(input: String): List<String>? {
        return null
    }

    override fun convertToType(input: String): Unit {
    }

    fun invoke(input: String) {
        CommandExecutor(input.split(" "), block).invoke()
    }

    override fun toString(): String {
        return " : $description"
    }
}
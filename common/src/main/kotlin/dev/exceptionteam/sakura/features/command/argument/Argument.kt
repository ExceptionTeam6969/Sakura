package dev.exceptionteam.sakura.features.command.argument

abstract class Argument<T>(
    val index: Int
) {
    abstract fun complete(input: String): List<String>?

    abstract fun convertToType(input: String): T?

    open fun check(input: String): Boolean {
        return runCatching {
            convertToType(input) != null
        }.getOrElse { false }
    }
}
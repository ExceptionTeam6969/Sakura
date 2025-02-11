package dev.exceptionteam.sakura.features.command.argument.impl

import dev.exceptionteam.sakura.features.command.argument.Argument

class StringArgument(
    index: Int,
    val name: String,
    val alias: Array<out String>,
    private val ignoreCase: Boolean
) : Argument<String>(index) {
    override fun complete(input: String): List<String> {
        return if (name.startsWith(input)) {
            listOf(name)
        } else {
            alias.filter {
                it.startsWith(input, ignoreCase)
            }
        }
    }

    override fun convertToType(input: String): String? {
        return if (input.equals(name, ignoreCase) || alias.contains(input)) {
            input
        } else {
            null
        }
    }

    override fun toString(): String {
        return name
    }
}
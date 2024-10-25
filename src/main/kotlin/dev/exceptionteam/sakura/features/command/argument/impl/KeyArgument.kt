package dev.exceptionteam.sakura.features.command.argument.impl

import dev.exceptionteam.sakura.features.command.argument.Argument
import dev.exceptionteam.sakura.utils.control.KeyUtils

class KeyArgument(index: Int) : Argument<Int>(index) {
    override fun complete(input: String): List<String> {
        return listOf("[Key]")
    }

    override fun convertToType(input: String): Int {
        val keyCode = KeyUtils.parseToKeyCode(input.uppercase())

        if (keyCode == -1) {
            throw Exception("Unknown key name")
        }

        return keyCode
    }

    override fun toString(): String {
        return "[Key]"
    }
}
package dev.exceptionteam.sakura.features.command.argument.impl

import dev.exceptionteam.sakura.features.command.argument.Argument
import dev.exceptionteam.sakura.managers.impl.FriendManager

class FriendArgument(index: Int) : Argument<String>(index) {
    override fun complete(input: String): List<String> {
        return FriendManager.friends
            .filter { it.startsWith(input, true) }
    }

    override fun convertToType(input: String): String {
        return FriendManager.friends.first {
            it.equals(input, ignoreCase = true)
        }
    }

    override fun toString(): String {
        return "[Friend]"
    }
}
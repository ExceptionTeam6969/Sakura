package dev.exceptionteam.sakura.features.command.impl

import dev.exceptionteam.sakura.features.command.*
import dev.exceptionteam.sakura.managers.impl.FriendManager
import dev.exceptionteam.sakura.utils.ingame.ChatUtils

object FriendCommand : Command("friend", arrayOf("f"), "Friend commands") {
    init {
        literal {
            match("add") {
                any { anyArgument ->
                    executor("Add player to your friend list.") {
                        addFriend(anyArgument.value())
                    }
                }
            }

            match("del") {
                any { anyArgument ->
                    executor("Remove your friend from friend list.") {
                        removeFriend(anyArgument.value())
                    }
                }

                friend { friendArgument ->
                    executor("Remove your friend from friend list.") {
                        removeFriend(friendArgument.value())
                    }
                }
            }
        }
    }

    private fun addFriend(value: String) {
        FriendManager.addFriend(value)
        ChatUtils.sendMessage("Added friend $value.")
    }


    private fun removeFriend(value: String) {
        FriendManager.removeFriend(value)
        ChatUtils.sendMessage("Removed friend $value")
    }
}
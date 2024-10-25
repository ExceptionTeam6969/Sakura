package dev.exceptionteam.sakura.managers.impl

object FriendManager {

    val friends = mutableListOf<String>()

    /**
     * Add a new friend to the list of friends.
     * @param name The name of the new friend.
     * @return True if the friend was added, false if the friend already exists in the list.
     */
    fun addFriend(name: String): Boolean {
        friends.find { it == name }?.let {
            friends.add(name)
            return true
        }
        return false
    }

    /**
     * Remove a friend from the list of friends.
     * @param name The name of the friend to remove.
     * @return True if the friend was removed, false if the friend was not found in the list.
     */
    fun removeFriend(name: String): Boolean {
        friends.find { it == name }?.let {
            friends.remove(it)
            return true
        }
        return false
    }

    /**
     * Check if a name is a friend.
     * @param name The name to check.
     * @return True if the name is a friend, false otherwise.
     */
    fun isFriend(name: String): Boolean {
        return friends.contains(name)
    }

}
package dev.exceptionteam.sakura.managers.impl

object FriendManager {

    val friends = mutableListOf<String>()

    /**
     * Add a new friend to the list of friends.
     * @param name The name of the new friend.
     * @return True if the friend was added, false if the friend already exists in the list.
     */
    fun addFriend(name: String): Boolean {
        if (isFriend(name)) return false
        friends.add(name)
        return true
    }

    /**
     * Remove a friend from the list of friends.
     * @param name The name of the friend to remove.
     * @return True if the friend was removed, false if the friend was not found in the list.
     */
    fun removeFriend(name: String): Boolean {
        if (!isFriend(name)) return false
        friends.remove(name)
        return true
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
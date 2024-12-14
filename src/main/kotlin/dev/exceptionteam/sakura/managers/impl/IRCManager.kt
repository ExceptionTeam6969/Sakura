package dev.exceptionteam.sakura.managers.impl

object IRCManager {

    private const val MAX_MESSAGES = 100

    private val messages = mutableListOf<IRCMessage>()

    /**
     * Adds a new message to the messages list. If the list exceeds the maximum size, the last message is removed.
     * @param message The new message to add.
     */
    fun addMessage(message: IRCMessage) {
        messages.add(message)
        if (messages.size > MAX_MESSAGES) messages.removeLast()
    }

    /**
     * Returns the list of messages.
     */
    fun getMessages(): List<IRCMessage> = messages.toList()

    /**
     * Clears the list of messages.
     */
    fun clearMessages() {
        messages.clear()
    }

    init {

        // TODO: Receives messages from the IRC server and adds them to the messages list.

    }

    data class IRCMessage(
        val sender: String,
        val message: String,
    )

}
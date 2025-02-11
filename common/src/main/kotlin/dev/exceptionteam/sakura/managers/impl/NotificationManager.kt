package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.features.modules.impl.client.ChatNotification
import dev.exceptionteam.sakura.graphics.easing.AnimationFlag
import dev.exceptionteam.sakura.graphics.texture.Texture
import dev.exceptionteam.sakura.utils.ingame.ChatUtils

object NotificationManager {

    private const val MAX_NOTIFICATIONS = 8

    val notifications = mutableListOf<Notification>()

    fun addNotification(notification: Notification) {
        synchronized(notifications) {
            notifications.add(notification)
            if (notifications.size > MAX_NOTIFICATIONS) {
                notifications.removeLastOrNull()
            }
        }

        if (ChatNotification.isEnabled) ChatUtils.sendMessageWithID(notification.message, notification.id)
    }

    fun addNotification(title: String, message: String, icon: Texture? = null, id: Int? = null) =
        addNotification(
            if (id == null) Notification(title, message, icon)
            else Notification(title, message, icon, id)
        )

    fun NonNullContext.addNotification(notification: Notification) =
        NotificationManager.addNotification(notification)

    fun NonNullContext.addNotification(title: String, message: String, icon: Texture? = null, id: Int? = null) =
        addNotification(
            if (id == null) Notification(title, message, icon)
            else Notification(title, message, icon, id)
        )

    data class Notification(
        val title: String,
        val message: String,
        val icon: Texture? = null,
        val id: Int = "$title*$message".hashCode(),
        var animations: NotificationAnimations? = null,
        val addTime: Long = System.currentTimeMillis()
    )
    data class NotificationAnimations(
        val mainEasing: AnimationFlag,
        val keepEasing: AnimationFlag,
        val keeping: Boolean = false,
    )

}
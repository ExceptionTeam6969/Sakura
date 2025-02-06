package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.ScissorBox
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.easing.AnimationFlag
import dev.exceptionteam.sakura.graphics.easing.Easing
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.graphics.utils.RenderUtils2D
import dev.exceptionteam.sakura.managers.impl.NotificationManager
import net.minecraft.client.Minecraft

object NotificationHUD: HUDModule(
    name = "notification-hud",
) {
    private val width0 by setting("width", 170f, 50f..400f)
    private val height0 by setting("height", 36f, 30f..100f)
    private val mainLength by setting("main-length", 300f, 100f..1000f)
    private val keepLength by setting("keep-length", 1500f, 100f..1500f)
    private val backgroundColor by setting("background-color", ColorRGB(0, 0, 0, 160))
    private val lineColor by setting("line-color", ColorRGB(255, 255, 255))
    private val shadow by setting("shadow", false)

    override var width: Float = 0f ;get() = width0
    override var height: Float = 0f ;get() = height0

    private val mc: Minecraft get() = Minecraft.getInstance()

    override fun render() {
        synchronized(NotificationManager.notifications) {
            val isUp = isDirUp()    // Get the direction of the notification

            ScissorBox(x, 0f, width, mc.window.height.toFloat()).draw {
                val removeList = mutableListOf<NotificationManager.Notification>()

                NotificationManager.notifications.forEachIndexed { index, notification ->
                    notification.animations?.let {
                        // Render notification
                        if (renderNotification(notification, index, isUp))
                            removeList.add(notification)
                    } ?: run {
                        notification.animations = NotificationManager.NotificationAnimations(
                            mainEasing = AnimationFlag(Easing.LINEAR, mainLength),
                            keepEasing = AnimationFlag(Easing.LINEAR, mainLength + keepLength),
                        )
                    }
                }

                NotificationManager.notifications.removeAll(removeList)
            }
        }
    }

    /**
     * Get the direction of the notification based on the y position of the notification.
     * @return true up, false down
     */
    private fun isDirUp(): Boolean =
        if (y > mc.window.guiScaledHeight / 2) true else false

    /**
     * @return true if it should be removed, false otherwise
     */
    private fun renderNotification(
        notification: NotificationManager.Notification,
        index: Int,
        isUp: Boolean
    ): Boolean {
        val animations = notification.animations ?: return false

        val y = if (isUp) {
            this.y - index * height
        } else {
            this.y + index * height
        }

        // Render notification
        var flag = false

        val widthPercent = animations.mainEasing.getAndUpdate(
            if (animations.keepEasing.getAndUpdate(1f) > 0.99f) {
                flag = true
                0f
            }
            else 1f
        )

        val x = width - widthPercent * width + this.x

        val iconScale = notification.icon?.let {
            (height - 6f) / it.height.toFloat()
        } ?: 1f
        val iconWidth = notification.icon?.width?.times(iconScale) ?: 0f
        val iconHeight = notification.icon?.height?.times(iconScale) ?: 0f

        RenderUtils2D.drawRectFilled(
            x, y, width, height, backgroundColor
        )

        notification.icon?.let {
            RenderUtils2D.drawTextureRect(
                x + 3, y + 3, iconWidth, iconHeight, it
            )
        }

        FontRenderers.drawString(notification.title, x + 6 + iconWidth, y + 5, ColorRGB.WHITE, shadow, 1.2f)

        FontRenderers.drawString(notification.message,
            x + 6 + iconWidth, y + height - FontRenderers.getHeight() - 5,
            ColorRGB.WHITE, shadow, 1.0f
        )

        RenderUtils2D.drawRectFilled(
            x, y + height - 2, width * animations.keepEasing.get(), 2f, lineColor
        )

        return flag && widthPercent < 0.01f
    }
}
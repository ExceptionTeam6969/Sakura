package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.gui.shared.component.AbstractComponent
import dev.exceptionteam.sakura.features.gui.shared.component.ModuleComponent.Companion.lastWindow
import dev.exceptionteam.sakura.features.gui.shared.component.ModuleComponent.Companion.positionAnimationFlag
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.translation.TranslationString
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import kotlin.math.max
import kotlin.math.min

abstract class Window(
    var title: TranslationString, x: Float, y: Float, width: Float, val compHeight: Float
) : AbstractComponent(x, y, width, 0f) {
    protected val components = mutableListOf<AbstractComponent>()

    fun addComponent(child: AbstractComponent) {
        this.components.add(child)/* Don't need to update height, because it will be updated when rendering */
        width = components.maxOfOrNull { it.width } ?: 10f
    }

    open fun onOpen() {}

    open fun onClose() {}

    override fun updatePosition(x: Float, y: Float) {
        super.updatePosition(x, y)

        var offsetY = compHeight    // Title
        components.forEach {
            if (!it.visible) return@forEach
            it.updatePosition(x, y + offsetY)
            offsetY += it.height
        }
        height = offsetY
    }

    private fun lerpPosition(first: Float, next: Float, progress: Float): Float {
        val start = min(first, next)
        val end = max(first, next)
        return if (first == next) {
            next
        } else {
            start + (end - start) * if (first >= next) progress else (1 - progress)
        }
    }

    override fun render() {
        val progress = positionAnimationFlag.getAndUpdate(1f)//if (lastWindow?.x == x && lastWindow?.y == y) 1f else 0f).coerceIn(0f, 1f)
        val finalX = lastWindow?.let { lerpPosition(x, it.x, progress) } ?: x
        val finalY = lastWindow?.let { lerpPosition(y, it.y, progress) } ?: y
        ChatUtils.sendNoSpamMessage(progress.toString())
        updatePosition(finalX, finalY)

        RenderUtils2D.drawRectFilled(finalX, finalY, width * progress, height * progress, UiSetting.primaryColor)
        RenderUtils2D.drawRectOutline(finalX, finalY, width * progress, height * progress, UiSetting.outlineColor)

        val newColor = ColorRGB(
            UiSetting.textColor.r,
            UiSetting.textColor.g,
            UiSetting.textColor.b,
            (UiSetting.textColor.a * progress).toInt()
        )
        FontRenderers.drawString(title, finalX + 5f, finalY + 4f, newColor)

        components.forEach {
            if (!it.visible) return@forEach
            it.mouseX = mouseX
            it.mouseY = mouseY
            it.checkHovering()
            it.render()
        }
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        return components.filter { it.isHovering && it.visible }.getOrNull(0)?.mouseClicked(type) == true
    }

    override fun mouseReleased(type: MouseButtonType): Boolean {/* Don't need to check hovering, because in some components,
           mouseReleased is called even if the mouse is not hovering */
        var processed = false
        components.filter { it.visible }.forEach {
            if (it.mouseReleased(type)) {
                processed = true
            }
        }

        return processed
    }
}
package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.gui.shared.component.AbstractComponent
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.translation.TranslationString
import dev.exceptionteam.sakura.utils.control.MouseButtonType

abstract class Window(
    var title: TranslationString,
    x: Float, y: Float, width: Float, val compHeight: Float
) : AbstractComponent(x, y, width, 0f) {
    protected val components = mutableListOf<AbstractComponent>()

    fun addComponent(child: AbstractComponent) {
        this.components.add(child)
        /* Don't need to update height, because it will be updated when rendering */
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

    override fun render() {
        RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB.WHITE)
        RenderUtils2D.drawRectOutline(x, y, width, height, ColorRGB.BLACK)

        FontRenderers.drawString(title, x + 5f, y + 4f, ColorRGB.BLACK)

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

    override fun mouseReleased(type: MouseButtonType): Boolean {
        /* Don't need to check hovering, because in some components,
           mouseReleased is called even if the mouse is not hovering */
        return components.filter { it.visible }.getOrNull(0)?.mouseReleased(type) == true
    }
}
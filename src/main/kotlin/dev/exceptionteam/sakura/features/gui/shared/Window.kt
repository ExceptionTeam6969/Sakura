package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.gui.shared.component.AbstractComponent
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.translation.TranslationString
import dev.exceptionteam.sakura.utils.control.MouseButtonType

abstract class Window(
    var title: TranslationString,
    x: Float, y: Float, width: Float, height: Float
) : AbstractComponent(x, y, width, height) {
    private val components = mutableListOf<AbstractComponent>()

    fun addComponent(child: AbstractComponent) {
        this.components.add(child)

        height += child.height
        width = components.maxOfOrNull { it.width } ?: 10f
    }

    override fun updatePosition(x: Float, y: Float) {
        super.updatePosition(x, y)

        var offsetY = height
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
        return components.filter { it.isHovering && it.visible }.getOrNull(0)?.mouseReleased(type) == true
    }
}
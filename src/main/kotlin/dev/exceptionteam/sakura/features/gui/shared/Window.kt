package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.gui.shared.component.AbstractComponent
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.control.MouseButtonType

abstract class Window(x: Float, y: Float, width: Float, height: Float) : AbstractComponent(x, y, width, height) {
    private val components = mutableListOf<AbstractComponent>()

    fun addComponent(child: AbstractComponent) {
        this.components.add(child)

        height += child.height
        width = components.maxOfOrNull { it.width } ?: 10f
    }

    override fun updatePosition(x: Float, y: Float) {
        super.updatePosition(x, y)

        var offsetY = 0f
        components.forEach {
            it.updatePosition(x, y + offsetY)
            offsetY += it.width
        }
    }

    override fun render() {
        RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB.WHITE)
        RenderUtils2D.drawRectOutline(x, y, width, height, ColorRGB.BLACK)

        components.forEach {
            it.render()
        }
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
//        TODO Event Delivery
        return false
    }

    override fun mouseReleased(type: MouseButtonType): Boolean {
//        TODO Event Delivery
        return false
    }
}
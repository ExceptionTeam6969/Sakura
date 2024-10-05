package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.gui.shared.component.ModuleComponent
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class Panel(
    val category: Category,
    val modules: List<AbstractModule>,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
) {

    private var isDragging = false
    private var isHovering = false
    private var isOpening = true

    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    private val moduleComponents = mutableListOf<ModuleComponent>()

    init {
        var offset = height
        modules.forEach {
            moduleComponents.add(ModuleComponent(it, x, y + offset, width, height))
            offset += height
        }
    }

    fun render(mouseX: Float, mouseY: Float) {

        checkHovering(mouseX, mouseY)

        if (isDragging) {
            x = mouseX - dragOffsetX
            y = mouseY - dragOffsetY

            moduleComponents.forEach {
                it.updatePosition(x, y)
            }
        }

        RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB.WHITE)

        FontRenderers.default.drawString(category.translation, x + 5f, y + 2f, ColorRGB.BLACK)

        if (!isOpening) return

        moduleComponents.forEach {
            it.mouseX = mouseX; it.mouseY = mouseY
            it.checkHovering()
            it.render()
        }
    }

    fun mouseClicked(mouseX: Float, mouseY: Float, type: MouseButtonType) {
        if (isHovering && type == MouseButtonType.LEFT) {
            isDragging = true
            dragOffsetX = mouseX - x
            dragOffsetY = mouseY - y
        }

        if (isHovering && type == MouseButtonType.RIGHT) {
            isOpening = !isOpening
        }

        moduleComponents.forEach {
            it.mouseClicked(type)
        }
    }

    fun mouseReleased(type: MouseButtonType) {
        if (type == MouseButtonType.LEFT) {
            isDragging = false
        }

        moduleComponents.forEach {
            it.mouseReleased(type)
        }
    }

    private fun checkHovering(mouseX: Float, mouseY: Float) {
        isHovering = mouseX in x..(x + width) && mouseY in y..(y + height)
    }

}
package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import net.minecraft.client.gui.DrawContext

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
    private var isOpening = false

    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    fun render(mouseX: Float, mouseY: Float) {

        checkHovering(mouseX, mouseY)

        if (isDragging) {
            x = mouseX - dragOffsetX
            y = mouseY - dragOffsetY
        }

        RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB.WHITE)

        FontRenderers.default.drawString(category.displayString, x + 5f, y + 2f, ColorRGB.BLACK)

        if (!isOpening) return

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
    }

    fun mouseReleased(type: MouseButtonType) {
        if (type == MouseButtonType.LEFT) {
            isDragging = false
        }
    }

    private fun checkHovering(mouseX: Float, mouseY: Float) {
        isHovering = mouseX in x..(x + width) && mouseY in y..(y + height)
    }

}
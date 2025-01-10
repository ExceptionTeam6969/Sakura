package dev.exceptionteam.sakura.features.gui.shared

import dev.exceptionteam.sakura.features.gui.shared.component.ModuleComponent
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.color.RainbowColor
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

            var offset = height
            moduleComponents.forEach {
                it.updatePosition(x, y + offset)
                offset += it.height
            }
        }

        if (UiSetting.rounded) {
            val rainbowColor = RainbowColor.getRainbow(0, 0.55f, 1f)
            RenderUtils2D.drawDynamicIsland(
                x = x,
                y = y,
                width = width,
                height = height,
                radius = (height) / 2,
                segments = 100,
                color = ColorRGB(rainbowColor.red, rainbowColor.green, rainbowColor.blue, rainbowColor.alpha),
                filled = true
            )
        } else {
            RenderUtils2D.drawRectFilled(x, y, width, height, UiSetting.primaryColor)
        }

        val length = FontRenderers.getStringWidth(category.translation)
        FontRenderers.drawString(category.translation, (x + width / 2 - length / 2f), y + 4f, UiSetting.textColor)

        if (!isOpening) return

        RenderUtils2D.drawRectFilled(x, y + height, width, moduleComponents.size * height, UiSetting.primaryColor)

        moduleComponents.forEach {
            it.mouseX = mouseX; it.mouseY = mouseY
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

    fun mouseScrolled(scrollX: Float, scrollY: Float) {
        x += scrollX.toFloat()
        y += scrollY.toFloat()

        moduleComponents.forEach {
            it.updatePosition(it.x + scrollX.toFloat(), it.y + scrollY.toFloat())
        }
    }

}
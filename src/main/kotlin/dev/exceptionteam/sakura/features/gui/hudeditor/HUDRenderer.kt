package dev.exceptionteam.sakura.features.gui.hudeditor

import dev.exceptionteam.sakura.features.gui.shared.component.AbstractComponent
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import net.minecraft.client.MinecraftClient

class HUDRenderer(
    x: Float, y: Float, width: Float, height: Float, val module: HUDModule
): AbstractComponent(x, y, width, height) {

    companion object {
        val mc = MinecraftClient.getInstance()
    }

    private var isDragging = false

    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    override fun render() {
        if (isDragging) {
            val x = (mouseX - dragOffsetX).coerceIn(0f, mc.window.scaledWidth - width)
            val y = (mouseY - dragOffsetY).coerceIn(0f, mc.window.scaledHeight - height)
            module.setPosition(x, y)
            updatePosition(x, y)
        }

        module.let {
            RenderUtils2D.drawRectFilled(it.x, it.y, it.width, it.height, ColorRGB(0, 0, 0, 96))
            it.render()
        }
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (isHovering && type == MouseButtonType.LEFT) {
            isDragging = true
            dragOffsetX = mouseX - x
            dragOffsetY = mouseY - y
            return true
        }
        return false
    }

    override fun mouseReleased(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            isDragging = false
            return true
        }
        return false
    }

}
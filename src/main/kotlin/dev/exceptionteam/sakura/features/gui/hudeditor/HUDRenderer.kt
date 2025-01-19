package dev.exceptionteam.sakura.features.gui.hudeditor

import dev.exceptionteam.sakura.features.gui.shared.component.AbstractComponent
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.utils.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import net.minecraft.client.Minecraft

class HUDRenderer(
    x: Float, y: Float, val module: HUDModule
): AbstractComponent(x, y) {

    override var width: Float = 0f
        get() = module.width
    override var height: Float = 0f
        get() = module.height

    companion object {
        val mc: Minecraft = Minecraft.getInstance()
    }

    private var isDragging = false

    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    override fun render() {
        if (isDragging) {
            val x = (mouseX - dragOffsetX).coerceIn(0f, mc.window.guiScaledWidth - width)
            val y = (mouseY - dragOffsetY).coerceIn(0f, mc.window.guiScaledHeight - height)
            module.x = x
            module.y = y
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
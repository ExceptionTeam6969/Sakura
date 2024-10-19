package dev.exceptionteam.sakura.features.gui.clickgui

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.gui.shared.AbstractGUIScreen
import dev.exceptionteam.sakura.features.gui.shared.Panel
import dev.exceptionteam.sakura.features.gui.shared.Window
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.impl.client.ClickGUI
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.CopyOnWriteArrayList

object ClickGUIScreen : AbstractGUIScreen("ClickGUI") {

    private val panels = CopyOnWriteArrayList<Panel>()

    private var mouseX: Float = 0f
    private var mouseY: Float = 0f

    var currentWindow: Window? = null
        set(value) {
            if (value != null) {
                value.onOpen()
            } else {
                field?.onClose()
            }
            field = value
        }

    init {
        var xOffset = 10f

        nonNullListener<Render2DEvent>(alwaysListening = true) { e ->
            if (mc.currentScreen !is ClickGUIScreen) return@nonNullListener
            if (ClickGUI.background) {
                RenderUtils2D.drawRectGradientV(
                    0f, 0f, mc.window.scaledWidth.toFloat(),
                    mc.window.scaledHeight.toFloat(),
                    ClickGUI.backgroundColor.alpha(0.1f), ClickGUI.backgroundColor.alpha(0.8f)
                )
            }
            panels.forEach { it.render(mouseX, mouseY) }

            currentWindow?.render()
        }

        Category.entries
            .filter { it != Category.HUD }
            .forEach { cate ->
                val modules = ModuleManager.modules.filter {
                    it.category == cate
                }

                panels.add(Panel(cate, modules, xOffset, 10f, WIDTH, HEIGHT))

                xOffset += WIDTH + 10f
            }
    }

    override fun close() {
        ClickGUI.disable()
        super.close()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val xf = mouseX.toFloat()
        val yf = mouseY.toFloat()

        currentWindow?.let {
            it.mouseY = yf
            it.mouseX = xf
        }

        this@ClickGUIScreen.mouseX = xf
        this@ClickGUIScreen.mouseY = yf
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val type = when (button) {
            0 -> MouseButtonType.LEFT
            1 -> MouseButtonType.RIGHT
            else -> MouseButtonType.NONE
        }

        currentWindow?.let {
            if (!it.checkHovering()) {
                currentWindow = null
                return@let
            }

            if (it.mouseClicked(type)) {
                return true
            }
        }

        panels.forEach { it.mouseClicked(mouseX.toFloat(), mouseY.toFloat(), type) }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val type = when (button) {
            0 -> MouseButtonType.LEFT
            1 -> MouseButtonType.RIGHT
            else -> MouseButtonType.NONE
        }

        currentWindow?.let {
            if (it.mouseReleased(type)) {
                return true
            }
        }

        panels.forEach { it.mouseReleased(type) }
        return super.mouseReleased(mouseX, mouseY, button)
    }

}
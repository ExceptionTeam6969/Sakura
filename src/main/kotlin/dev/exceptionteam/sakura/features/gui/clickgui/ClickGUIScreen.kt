package dev.exceptionteam.sakura.features.gui.clickgui

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.gui.shared.GuiScreen
import dev.exceptionteam.sakura.features.gui.shared.Panel
import dev.exceptionteam.sakura.features.gui.shared.Window
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.impl.client.ClickGUI
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.features.modules.impl.hud.GuiImage
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import net.minecraft.client.gui.GuiGraphics
import java.util.concurrent.CopyOnWriteArrayList

object ClickGUIScreen : GuiScreen("click-gui") {

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

        nonNullListener<Render2DEvent>(alwaysListening = true, priority = -100) { e ->
            if (mc.screen !is ClickGUIScreen) return@nonNullListener
            if (UiSetting.background) {
                RenderUtils2D.drawRectGradientV(
                    0f, 0f, mc.window.guiScaledWidth.toFloat(),
                    mc.window.guiScaledHeight.toFloat(),
                    UiSetting.backgroundColor.alpha(0.1f), UiSetting.backgroundColor.alpha(0.8f)
                )
            }

            GuiImage.renderImage()

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

                xOffset += WIDTH + 5f
            }
    }

    override fun onClose() {
        ClickGUI.disable()
        super.onClose()
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
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
                //currentWindow = null
                it.readyToClose = true
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

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        currentWindow?.onMouseScrolled(scrollX, scrollY)
        panels.forEach {
            it.mouseScrolled(-scrollX.toFloat() * 5, -scrollY.toFloat() * 5)
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
    }

}
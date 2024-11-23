package dev.exceptionteam.sakura.features.gui.hudeditor

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.gui.shared.GuiScreen
import dev.exceptionteam.sakura.features.gui.shared.Panel
import dev.exceptionteam.sakura.features.gui.shared.Window
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.features.modules.impl.client.HUDEditor
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.features.modules.impl.hud.GuiImage
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.CopyOnWriteArrayList

object HUDEditorScreen : GuiScreen("hud-editor") {

    private val panel: Panel
    private val hudRenderers = CopyOnWriteArrayList<HUDRenderer>()

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

        val modules = ModuleManager.modules.filter { it.category == Category.HUD }
        panel = Panel(Category.HUD, modules, 10f, 10f, WIDTH, HEIGHT)
        modules.forEach {
            if (it !is HUDModule) return@forEach
            hudRenderers.add(HUDRenderer(it.x, it.y, it.width, it.height,it))
        }

        nonNullListener<Render2DEvent>(alwaysListening = true, priority = -50) { e ->
            if (mc.currentScreen !is HUDEditorScreen) return@nonNullListener
            if (UiSetting.background) {
                RenderUtils2D.drawRectGradientV(
                    0f, 0f, mc.window.scaledWidth.toFloat(),
                    mc.window.scaledHeight.toFloat(),
                    UiSetting.backgroundColor.alpha(0.1f), UiSetting.backgroundColor.alpha(0.8f)
                )
            }

            GuiImage.renderImage()

            hudRenderers.filter { it.module.isEnabled }.forEach {
                it.mouseX = mouseX
                it.mouseY = mouseY
                it.checkHovering()
                it.render()
            }

            panel.render(mouseX, mouseY)

            currentWindow?.render()
        }

    }

    override fun close() {
        HUDEditor.disable()
        super.close()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val xf = mouseX.toFloat()
        val yf = mouseY.toFloat()

        currentWindow?.let {
            it.mouseY = yf
            it.mouseX = xf
        }

        this@HUDEditorScreen.mouseX = xf
        this@HUDEditorScreen.mouseY = yf
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

        hudRenderers.filter { it.module.isEnabled }.forEach { it.mouseClicked(type) }
        panel.mouseClicked(mouseX.toFloat(), mouseY.toFloat(), type)
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

        hudRenderers.filter { it.module.isEnabled }.forEach { it.mouseReleased(type) }
        panel.mouseReleased(type)
        return super.mouseReleased(mouseX, mouseY, button)
    }

}
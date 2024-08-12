package dev.exceptionteam.sakura.features.gui.clickgui

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.impl.client.ClickGUI
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.CopyOnWriteArrayList

object ClickGUIScreen: AbstractGUIScreen("ClickGUI") {

    private val panels = CopyOnWriteArrayList<Panel>()

    private var mouseX: Float = 0f
    private var mouseY: Float = 0f

    init {
        var xOffset = 10f

        nonNullListener<Render2DEvent>(alwaysListening = true) { e ->
            if (mc.currentScreen !is ClickGUIScreen) return@nonNullListener
            panels.forEach { it.render(e.context, mouseX, mouseY) }
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
        this@ClickGUIScreen.mouseX = mouseX.toFloat()
        this@ClickGUIScreen.mouseY = mouseX.toFloat()
    }

}
package dev.exceptionteam.sakura.features.gui.clickgui

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.impl.client.ClickGUI
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.CopyOnWriteArrayList

object ClickGUIScreen: AbstractGUIScreen("ClickGUI") {

    private val panels = CopyOnWriteArrayList<Panel>()

    init {
        var xOffset = 10f

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
        panels.forEach { it.render(context, mouseX.toFloat(), mouseY.toFloat()) }
        super.render(context, mouseX, mouseY, delta)
    }

}
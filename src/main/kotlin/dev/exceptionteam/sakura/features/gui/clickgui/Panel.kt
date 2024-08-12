package dev.exceptionteam.sakura.features.gui.clickgui

import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import net.minecraft.client.gui.DrawContext

class Panel(
    val category: Category,
    val modules: List<AbstractModule>,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
) {

    fun render(context: DrawContext, mouseX: Float, mouseY: Float) {

        RenderUtils2D.drawRectFilled(0f, 0f, 1000f, 1000f, ColorRGB.WHITE)

    }

}
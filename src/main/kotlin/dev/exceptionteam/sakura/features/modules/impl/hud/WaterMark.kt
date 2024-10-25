package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers

object WaterMark: HUDModule(
    name = "water-mark",
    width = 20f,
    height = 20f,
) {

    private val version by setting("version", true)
    private val textColor by setting("text-color", ColorRGB.WHITE)

    override fun render() {
        val text = Sakura.NAME + if (version) " ${Sakura.VERSION}" else ""

        FontRenderers.drawString(text, x, y, textColor)
    }

}
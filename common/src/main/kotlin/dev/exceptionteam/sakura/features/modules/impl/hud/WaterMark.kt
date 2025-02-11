package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers

object WaterMark: HUDModule(
    name = "water-mark",
) {

    private val version by setting("version", true)
    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)

    var text: String? = null

    override var height: Float = 12f
        get() = (12f * scale)

    override var width: Float = 50f
        get() = FontRenderers.getStringWidth(text ?: "", scale)

    override fun render() {
        text = Sakura.NAME + if (version) " ${Sakura.VERSION}" else ""
        FontRenderers.drawString(text!!, x, y, textColor, shadow, scale)
    }

}
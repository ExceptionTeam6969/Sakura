package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.Wrapper.player

object Welcomer: HUDModule(
    name = "welcomer",
) {

    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)

    var text: String? = null

    override var height: Float = 12f
        get() = (12f * scale)

    override var width: Float = 150f
        get() = (text?.length?.times(4.9f)?.times(scale)) ?: 40f

    override fun render() {
        text = "Welcome ${player?.name?.string}! | ${Sakura.NAME}"
        FontRenderers.drawString(text!!, x, y, textColor, shadow, scale)
    }

}
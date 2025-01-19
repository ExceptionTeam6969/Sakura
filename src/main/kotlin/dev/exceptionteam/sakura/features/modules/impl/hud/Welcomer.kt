package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.features.modules.impl.hud.WaterMark.setting
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.Wrapper.player

object Welcomer: HUDModule(
    name = "welcomer",
    width = 150f,
    height = 12f,
) {

    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)

    override fun render() {
        val text = "Welcome ${player?.name?.string}! | ${Sakura.NAME}"
        FontRenderers.drawString(text, x, y, textColor, shadow, scale)
    }

}
package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.mixins.core.Interface.MinecraftAccessor

object FPS: HUDModule(
    name = "fps",
) {
    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)

    var text: String? = null

    override var height: Float = 12f
        get() = (12f * scale)

    override var width: Float = 40f
        get() = FontRenderers.getStringWidth(text ?: "FPS: 0", scale)

    override fun render() {
        text = "FPS: ${MinecraftAccessor.getFps()}"
        FontRenderers.drawString(text!!, x, y, textColor, shadow, scale)
    }

}
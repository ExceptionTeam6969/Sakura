package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.ScissorBox
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.translation.TranslationString

object IrcHud: HUDModule(
    name = "irc-hud",
) {
    private val backgroundColor by setting("background-color", ColorRGB(0, 0, 0, 140))

    override var width by setting("width", 200f, 100f..500f)
    override var height by setting("height", 200f, 100f..500f)

    private val scissorBox = ScissorBox()

    override fun render() {
        scissorBox.updateAndDraw(x, y, width, height) {
            RenderUtils2D.drawRectFilled(x, y, width, height, backgroundColor)

            FontRenderers.drawString(TranslationString("irc", "hud-title"), x + 5, y + 5, ColorRGB(255, 255, 255))
            // TODO: Render IRC messages
        }
    }
}
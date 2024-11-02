package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.glyphs.FontChunks
import dev.exceptionteam.sakura.translation.TranslationString
import java.awt.Font

object FontRenderers {

    const val FONT_SIZE = 16f

    private val cnFont = FontChunks(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/chinese.ttf")
    ).deriveFont(Font.PLAIN, FONT_SIZE * 2f))

    // Comfortaa
    private val comFont = FontChunks(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/comfortaa.ttf")
    ).deriveFont(Font.PLAIN, FONT_SIZE * 2f))

    val default = FontRenderer(comFont)
    val chinese = FontRenderer(cnFont)

    fun drawString(text: String, x: Float, y: Float, color: ColorRGB, scale: Float = 1.0f): Float =
        default.drawString(text, x, y, color, scale, chinese)

    fun drawString(text: TranslationString, x: Float, y: Float, color: ColorRGB, scale: Float = 1.0f): Float =
        drawString(text.translation, x, y, color, scale)

    fun drawStringRev(text: String, x: Float, y: Float, color: ColorRGB, scale: Float = 1.0f): Float =
        default.drawStringRev(text, x, y, color, scale, chinese)

    fun drawStringRev(text: TranslationString, x: Float, y: Float, color: ColorRGB, scale: Float = 1.0f): Float =
        drawStringRev(text.translation, x, y, color, scale)

    fun getStringWidth(text: String, scale: Float = 1.0f): Float =
        default.getStringWidth(text, scale, chinese)

    fun getStringWidth(text: TranslationString, scale: Float = 1.0f): Float =
        getStringWidth(text.translation, scale)

}
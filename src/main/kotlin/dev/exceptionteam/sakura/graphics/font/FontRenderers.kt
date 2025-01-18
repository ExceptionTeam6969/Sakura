package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.translation.TranslationString
import java.awt.Font

object FontRenderers {

    const val FONT_SIZE = 16f

    // Comfortaa
    private val enFont = FontAdapter(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/font.ttf")
    ).deriveFont(Font.PLAIN, FONT_SIZE * 2f))

    private val default = FontRenderer(enFont)

    fun drawString(
        text: String, x: Float, y: Float,
        color: ColorRGB, shadow: Boolean = false,
        scale: Float = 1.0f
    ): Float =
        default.drawString(text, x, y, color, shadow, scale)

    fun drawString(
        text: TranslationString, x: Float, y: Float,
        color: ColorRGB, shadow: Boolean = false,
        scale: Float = 1.0f
    ): Float =
        drawString(text.translation, x, y, color, shadow, scale)

    fun drawStringRev(
        text: String, x: Float, y: Float,
        color: ColorRGB, shadow: Boolean = false,
        scale: Float = 1.0f
    ): Float =
        default.drawStringRev(text, x, y, color, shadow, scale)

    fun drawStringRev(
        text: TranslationString, x: Float, y: Float,
        color: ColorRGB, shadow: Boolean = false,
        scale: Float = 1.0f
    ): Float =
        drawStringRev(text.translation, x, y, color, shadow, scale)

    fun getStringWidth(text: String, scale: Float = 1.0f): Float =
        default.getStringWidth(text, scale)

    fun getStringWidth(text: TranslationString, scale: Float = 1.0f): Float =
        getStringWidth(text.translation, scale)

    fun getHeight(scale: Float = 1.0f): Float =
        default.getHeight(scale)

}
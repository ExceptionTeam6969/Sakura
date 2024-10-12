package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL45.*

class FontRenderer(
    private val font: FontGlyphs
) {

    fun drawString(
        text: String, x: Float, y: Float,
        color0: ColorRGB, scale0: Float = 1f, backFont: FontRenderer? = null
    ) {
        val length = text.length
        var shouldContinue = false
        var color = color0

        glEnable(GL_LINE_SMOOTH)

        val scale = scale0 / 40f * CustomFont.fontSize

        MatrixStack.use {
            for (i in 0 until length) {
                if (shouldContinue) {
                    shouldContinue = false
                    continue
                }
                if (text[i] == '§' && i < length - 1) {
                    shouldContinue = true
                    color = getColor(text[i + 1])
                    continue
                }

                val prevWidth =
                    if (canDisplay(text[i])) drawChar(text[i], x, y, color, scale)
                    else backFont?.drawChar(text[i], x, y, color, scale) ?: 0f

                translate(prevWidth, 0f, 0f)
            }
        }

        glDisable(GL_LINE_SMOOTH)
    }

    fun canDisplay(ch: Char): Boolean = font.canDisplay(ch)

    fun drawChar(ch: Char, x: Float, y: Float, color: ColorRGB, scale: Float): Float {
        val glyph = font.getGlyph(ch)

        val width = glyph.width * scale
        val height = glyph.height * scale

        RenderUtils2D.drawTextureRect(x, y, width, height, glyph.texture, color)

        return width
    }

    private fun getColor(ch: Char): ColorRGB =
        when (ch) {
            'r' -> ColorRGB(255, 0, 0)
            'g' -> ColorRGB(0, 255, 0)
            'b' -> ColorRGB(0, 0, 255)
            else -> ColorRGB.WHITE
        }

}
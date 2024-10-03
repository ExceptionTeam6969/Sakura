package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont
import dev.exceptionteam.sakura.graphics.RenderUtilsTexture
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL45

class FontRenderer(
    private val font: FontGlyphs
) {

    fun drawString(text: String, x: Float, y: Float, color0: ColorRGB, scale0: Float = 1f) {
        val length = text.length
        var shouldContinue = false
        var color = color0

        GL45.glEnable(GL45.GL_LINE_SMOOTH)

        val scale = scale0 / 32f * CustomFont.fontSize

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

                val prevWidth = drawChar(text[i], x, y, color, scale)

                translate(prevWidth + 1f, 0f, 0f)
            }
        }

        GL45.glDisable(GL45.GL_LINE_SMOOTH)
    }

    private fun drawChar(ch: Char, x: Float, y: Float, color: ColorRGB, scale: Float): Float {
        val glyph = font.getGlyph(ch)

        val width = glyph.width * scale
        val height = glyph.height * scale

        RenderUtilsTexture.drawTextureRect(x, y, width, height, glyph.texture, color)
//        RenderUtils2D.drawRectFilled(x, y, width, height, color)

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
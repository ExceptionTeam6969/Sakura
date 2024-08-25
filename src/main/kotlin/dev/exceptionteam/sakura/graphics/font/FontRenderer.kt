package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.graphics.RenderUtilsTexture
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL45

class FontRenderer(
    private val font: FontGlyphs
) {

    fun drawString(text: String, x: Float, y: Float, color0: ColorRGB) {
        val length = text.length
        var shouldContinue = false
        var color = color0

        GL45.glEnable(GL45.GL_LINE_SMOOTH)
        MatrixStack.push()
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

            val prevWidth = drawChar(text[i], x, y, color)

            MatrixStack.translate(prevWidth + 1f, 0f, 0f)
        }
        MatrixStack.pop()
        GL45.glDisable(GL45.GL_LINE_SMOOTH)
    }

    private fun drawChar(ch: Char, x: Float, y: Float, color: ColorRGB): Float {
        val glyph = font.getGlyph(ch)

        val width = glyph.dimensions.width.toFloat()
        val height = glyph.dimensions.height.toFloat()

        RenderUtilsTexture.drawTextureRect(x, y, width, height, glyph.textureId, color)
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
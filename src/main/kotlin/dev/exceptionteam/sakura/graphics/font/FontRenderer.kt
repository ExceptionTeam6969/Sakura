package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.RenderUtilsTexture
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL45

class FontRenderer(
    val font: Glyphs,
    private val defaultFont: Glyphs?
) {

    fun drawStringWithScale(text: String, x: Float, y: Float, color: ColorRGB, scale: Float = 1.0f) {

        MatrixStack.push()

        MatrixStack.scale(scale, scale, 1.0f)
        MatrixStack.translate((x / scale) - x, (y / scale) - y, 0f)
        drawString(text, x, y, color)

        MatrixStack.pop()

    }

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
        val texture = font.getTexture(ch)

        val width = getWidth(ch) * CustomFont.fontSize
        val height = font.getHeight(ch) * CustomFont.fontSize

        RenderUtilsTexture.drawTextureRect(x, y, width, height, texture, color)
//        RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB(0, 0, 0))

        return width
    }

    private fun getWidth(ch: Char): Float =
        if (font.canDisplay(ch)) getFontWidth(font, ch)
        else {
            defaultFont?.let { return getFontWidth(it, ch) }
            0f
        }

    private fun getFontWidth(font: Glyphs, ch: Char): Float = font.getWidth(ch).toFloat()

    private fun getColor(ch: Char): ColorRGB =
        when (ch) {
            'r' -> ColorRGB(255, 0, 0)
            'g' -> ColorRGB(0, 255, 0)
            'b' -> ColorRGB(0, 0, 255)
            else -> ColorRGB.WHITE
        }

}
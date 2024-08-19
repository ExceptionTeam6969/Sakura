package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.graphics.color.ColorRGB
import org.lwjgl.opengl.GL45

class FontRenderer(
    val font: Font,
    val defaultFont: Font
) {

    fun drawStringWithScale(text: String, x: Float, y: Float, color: ColorRGB, scale: Float = 1.0f) {

        GL45.glPushMatrix()
        GL45.glScalef(scale, scale, 1.0f)
        GL45.glTranslatef((x / scale) - x, (y / scale) - y, 0.0f)
        drawString(text, x, y, color)
        GL45.glPopMatrix()

    }

    fun drawString(text: String, x: Float, y: Float, color0: ColorRGB) {
        val length = text.length
        var shouldContinue = false
        var color = color0
        var offset = 0f
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

            drawChar(text[i], x + offset, y, color)

            offset += getWidth(text[i])
        }
    }

    private fun drawChar(ch: Char, x: Float, y: Float, color: ColorRGB) {
        val texture = font.getTexture(ch)
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, texture)

        
    }

    private fun getWidth(ch: Char): Float =
        if (font.font.canDisplay(ch)) getFontWidth(font, ch)
        else getFontWidth(defaultFont, ch)

    private fun getFontWidth(font: Font, ch: Char): Float = font.getWidth(ch).toFloat()

    private fun getColor(ch: Char): ColorRGB =
        when (ch) {
            'r' -> ColorRGB(255, 0, 0)
            'g' -> ColorRGB(0, 255, 0)
            'b' -> ColorRGB(0, 0, 255)
            else -> ColorRGB.WHITE
        }

}
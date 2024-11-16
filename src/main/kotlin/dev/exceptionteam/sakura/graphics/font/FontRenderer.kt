package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont
import dev.exceptionteam.sakura.graphics.GlHelper
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.glyphs.FontChunks
import dev.exceptionteam.sakura.graphics.font.glyphs.GlyphChunk
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack

class FontRenderer(
    private val font: FontChunks
) {

    /**
     * Draw a string with a custom font.
     * @param text The text to draw.
     * @param x The x position to start drawing.
     * @param y The y position to start drawing.
     * @param color0 The color of the text.
     * @param scale0 The scale of the text.
     * @param backFont The font to use for characters that cannot be displayed.
     * @return The width of the text.
     */
    fun drawString(
        text: String, x: Float, y: Float,
        color0: ColorRGB, scale0: Float = 1f, backFont: FontRenderer? = null
    ): Float {
        val length = text.length
        var shouldContinue = false
        var color = color0

        GlHelper.lineSmooth = true

        val scale = scale0 / 40f * CustomFont.fontSize

        var width = 0f

        MatrixStack.scope {
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

                width += prevWidth

                translate(prevWidth, 0f, 0f)
            }
        }

        GlHelper.lineSmooth = false

        return width
    }

    /**
     * Draw a string with a custom font, but reversed.
     */
    fun drawStringRev(
        text: String, x: Float, y: Float,
        color0: ColorRGB, scale0: Float = 1f, backFont: FontRenderer? = null
    ): Float {
        val width = getStringWidth(text, scale0, backFont)
        drawString(text, x - width, y, color0, scale0, backFont)
        return width
    }

    /**
     * Get the width of a string with a custom font.
     * @param text The text to measure.
     * @param scale The scale of the text.
     * @param backFont The font to use for characters that cannot be displayed.
     * @return The width of the text.
     */
    fun getStringWidth(text: String, scale0: Float = 1f, backFont: FontRenderer? = null): Float {
        var width = 0f
        var shouldContinue = false

        val scale = scale0 / 40f * CustomFont.fontSize

        for (i in 0 until text.length) {
            if (shouldContinue) {
                shouldContinue = false
                continue
            }
            if (text[i] == '§' && i < text.length - 1) {
                shouldContinue = true
                continue
            }

            val ch = text[i]

            val chunk = if (canDisplay(ch)) font.getChunk(ch.code / GlyphChunk.CHUNK_SIZE)
            else backFont?.font?.getChunk(ch.code / GlyphChunk.CHUNK_SIZE) ?: continue

            chunk.charData[ch]?.let {
                width += it.width * scale
            }
        }
        return width
    }

    fun canDisplay(ch: Char): Boolean = font.canDisplay(ch)

    fun drawChar(ch: Char, x: Float, y: Float, color: ColorRGB, scale: Float): Float {
        val chunk = font.getChunk(ch.code / GlyphChunk.CHUNK_SIZE)

        val charData = chunk.charData[ch] ?: return 0f

        val width = charData.width * scale
        val height = charData.height * scale

        RenderUtils2D.drawTextureRect(x, y, width, height,
            charData.uStart, charData.vStart, charData.uEnd, charData.vEnd,
            chunk.texture, color)

        return width
    }

    fun getHeight(scale: Float = 1f): Float {
        return font.getHeight() * scale / 40f * CustomFont.fontSize
    }

    private fun getColor(ch: Char): ColorRGB =
        when (ch) {
            'r' -> ColorRGB(255, 0, 0)
            'g' -> ColorRGB(0, 255, 0)
            'b' -> ColorRGB(0, 0, 255)
            else -> ColorRGB.WHITE
        }

}
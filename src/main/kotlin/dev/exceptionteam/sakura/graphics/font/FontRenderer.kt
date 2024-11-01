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

    fun drawString(
        text: String, x: Float, y: Float,
        color0: ColorRGB, scale0: Float = 1f, backFont: FontRenderer? = null
    ) {
        val length = text.length
        var shouldContinue = false
        var color = color0

        GlHelper.lineSmooth = true

        val scale = scale0 / 40f * CustomFont.fontSize

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

                translate(prevWidth, 0f, 0f)
            }
        }

        GlHelper.lineSmooth = false
    }

    fun canDisplay(ch: Char): Boolean = font.canDisplay(ch)

    fun getStringWidth(text: String, scale: Float = 1f): Float {
        var width = 0f
        var shouldContinue = false

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

            if (canDisplay(ch)) {
                val chunk = font.getChunk(ch.code / GlyphChunk.CHUNK_SIZE)

                chunk.charData[ch]?.let {
                    width += it.width * scale
                }
            }
        }
        return width
    }

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

    private fun getColor(ch: Char): ColorRGB =
        when (ch) {
            'r' -> ColorRGB(255, 0, 0)
            'g' -> ColorRGB(0, 255, 0)
            'b' -> ColorRGB(0, 0, 255)
            else -> ColorRGB.WHITE
        }

}
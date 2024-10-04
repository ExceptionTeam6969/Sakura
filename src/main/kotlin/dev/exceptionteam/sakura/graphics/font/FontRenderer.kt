package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import net.minecraft.client.MinecraftClient
import org.lwjgl.opengl.GL45.*

class FontRenderer(
    private val font: FontGlyphs
) {

    fun drawString(text: String, x: Float, y: Float, color0: ColorRGB, scale0: Float = 1f) {
        val length = text.length
        var shouldContinue = false
        var color = color0

        glEnable(GL_LINE_SMOOTH)

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

                translate(prevWidth, 0f, 0f)
            }
        }

        glDisable(GL_LINE_SMOOTH)
    }

    private fun drawChar(ch: Char, x: Float, y: Float, color: ColorRGB, scale: Float): Float {
        val glyph = font.getGlyph(ch)

        val width = glyph.width * scale
        val height = glyph.height * scale

//        RenderUtilsTexture.drawTextureRect(x, y, width, height, glyph.texture, color)
//        RenderUtils2D.drawRectFilled(x, y, width, height, color)
        val mc = MinecraftClient.getInstance()
        val glId = mc.textureManager.getTexture(glyph.imageTex).glId

        glBindTexture(GL_TEXTURE_2D, glId)

        val endX = x + width
        val endY = y + height

        GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosTex2D) {
            texture(endX, y, 1f, 0f, color)
            texture(x, y, 0f, 0f, color)
            texture(endX, endY, 1f, 1f, color)
            texture(x, endY, 0f, 1f, color)
        }

        glBindTexture(GL_TEXTURE_2D, 0)

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
package dev.exceptionteam.sakura.graphics.font.glyphs

import dev.exceptionteam.sakura.graphics.texture.ImageUtils
import dev.exceptionteam.sakura.graphics.texture.Texture
import org.lwjgl.opengl.GL45
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage

class GlyphChunk(
    val chunkId: Int,
    val font: Font,
    val size: Int,
) {

    val charData = mutableMapOf<Char, CharData>()

    val texture: Texture

    private val scaledOffset = (4 * size / 25f).toInt()

    init {
        val image = BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB)

        image.createGraphics().let {
            it.font = font

            it.background = Color(255, 255, 255, 0)
            it.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            it.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            it.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            it.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

            it.color = Color(255, 255, 255, 255)

            val metrics = it.fontMetrics
            val ascent = metrics.ascent

            var posX = 0
            var posY = 1
            var rowHeight = 0

            for (i in 0 until CHUNK_SIZE) {
                val ch = (chunkId * CHUNK_SIZE + i).toChar()
                val charWidth = metrics.charWidth(ch)
                val charHeight = metrics.height

                val imgWidth = charWidth + scaledOffset * 2

                if (posX + imgWidth > IMAGE_SIZE) {
                    posX = 0
                    posY += rowHeight
                    rowHeight = 0
                }

                if (rowHeight < charHeight) {
                    rowHeight = charHeight
                }

                val charData = CharData(charWidth.toFloat(), charHeight.toFloat())
                charData.uStart = (posX + scaledOffset) / IMAGE_SIZE.toFloat()
                charData.vStart = posY / IMAGE_SIZE.toFloat()
                charData.uEnd = (posX + scaledOffset + charData.width) / IMAGE_SIZE.toFloat()
                charData.vEnd = (posY + charData.height) / IMAGE_SIZE.toFloat()
                this.charData[ch] = charData

                it.drawString(ch.toString(), posX + scaledOffset, posY + ascent)
                posX += imgWidth
            }
        }

        texture = ImageUtils.uploadImageToTexture(image, GL45.GL_RGBA8)
    }

    companion object {
        const val CHUNK_SIZE = 64

        private const val IMAGE_SIZE = 512
    }

}
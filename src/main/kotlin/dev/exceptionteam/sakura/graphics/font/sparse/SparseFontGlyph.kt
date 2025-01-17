package dev.exceptionteam.sakura.graphics.font.sparse

import dev.exceptionteam.sakura.graphics.font.CharData
import dev.exceptionteam.sakura.graphics.texture.BindLessTexture
import dev.exceptionteam.sakura.graphics.texture.ImageUtils
import org.lwjgl.opengl.ARBSparseTexture.*
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage

class SparseFontGlyph(
    val font: Font,
    size: Float
) {

    val tex = BindLessTexture(GL_TEXTURE_2D_ARRAY)

    // Chunk -> (Char -> CharData)
    val charData = mutableMapOf<Int, MutableMap<Char, CharData>>()

    private val scaledOffset = (4 * size / 25f).toInt()

    var height: Float = 0f; private set

    init {
        glTextureParameteri(tex.id, GL_TEXTURE_SPARSE_ARB, GL_TRUE)

        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(tex.id, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTextureStorage3D(tex.id, 1, GL_RGBA8, IMAGE_SIZE, IMAGE_SIZE, MAX_CHUNKS)
    }

    fun getChunk(char: Char): Int = char.code / CHUNK_SIZE

    fun canDisplay(char: Char): Boolean = font.canDisplay(char)

    fun getCharData(char: Char): CharData? {
        if (charData[getChunk(char)] == null) {
            createChunk(getChunk(char))
        }
        return charData[getChunk(char)]?.get(char)
    }

    fun createChunk(chunk: Int) {
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
                val ch = (chunk * CHUNK_SIZE + i).toChar()
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
                this.charData.getOrPut(chunk) { mutableMapOf<Char, CharData>() }[ch] = charData

                it.drawString(ch.toString(), posX + scaledOffset, posY + ascent)
                posX += imgWidth
            }

            height = rowHeight.toFloat()
        }

        ImageUtils.uploadImageToSparseTexture(image, tex, chunk)
    }

    companion object {
        private const val MAX_CHUNKS = 1024

        private const val IMAGE_SIZE = 512
        const val CHUNK_SIZE = 64
    }

}
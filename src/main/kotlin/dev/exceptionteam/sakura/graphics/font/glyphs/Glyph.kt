package dev.exceptionteam.sakura.graphics.font.glyphs

import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import kotlin.math.ceil

class Glyph(
    private val font: Font,
    char: Char
) {

    val dimensions: Rectangle2D = font.getStringBounds(char.toString(), FontRenderContext(AffineTransform(), true, true))
    var textureId: Int = 0

    init {
        val bufferedImage = BufferedImage(ceil(dimensions.width).toInt(), ceil(dimensions.height).toInt(), BufferedImage.TYPE_INT_ARGB)
        bufferedImage.createGraphics().also {
            it.font = font

            it.color = Color(255, 255, 255, 0)
            it.fillRect(0, 0, bufferedImage.width, bufferedImage.height)

            it.color = Color.white
            it.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            it.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            it.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            it.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            it.drawString(char.toString(), 0, it.fontMetrics.ascent)
            it.dispose()
        }

        loadFromImage(bufferedImage)
    }

    fun destroy() {
        if (textureId != 0) {
            glDeleteTextures(textureId)
        }
    }

    private fun loadFromImage(image: BufferedImage) {

        val pixels = IntArray(image.width * image.height)
        image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)
        val buffer = ByteBuffer.allocateDirect(image.width * image.height * 4)

        for (h in 0 until image.height) {
            for (w in 0 until image.width) {
                val pixel = pixels[h * image.width + w]

                /* RGBA */
                buffer.put(((pixel shr 16) and 0xFF).toByte())  // Red
                buffer.put(((pixel shr 8) and 0xFF).toByte())   // Green
                buffer.put((pixel and 0xFF).toByte())           // Blue
                buffer.put(((pixel shr 24) and 0xFF).toByte())  // Alpha
            }
        }

        buffer.flip()

        textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height,
            0, GL_RGBA, GL_UNSIGNED_BYTE, buffer
        )

        glBindTexture(GL_TEXTURE_2D, 0)
    }

}
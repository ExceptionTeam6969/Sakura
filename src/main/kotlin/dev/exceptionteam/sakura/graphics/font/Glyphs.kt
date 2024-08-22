package dev.exceptionteam.sakura.graphics.font

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import kotlin.math.ceil

class Glyphs(
    private val font: Font
) {

    private val textureMap = mutableMapOf<Char, Int>() // Char & Texture id

    fun getTexture(c: Char): Int {
        textureMap[c]?.let { return it }
        val image = createFontTexture(c.toString())
        val id = createTextureFromImage(image)
        textureMap[c] = id
        return id
    }

    fun canDisplay(c: Char): Boolean = font.canDisplay(c.code)

    fun getWidth(c: Char): Float = getCharWidth(c)
    fun getHeight(c: Char): Float = getCharHeight(c)

    fun destroy() {
        textureMap.forEach { (_, it) ->
            GL45.glDeleteTextures(it)
        }
    }

    private fun getCharWidthHeight(ch: Char): Pair<Int, Int> {
        val dimensions = font.getStringBounds(ch.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )

        return Pair(dimensions.width.toInt(), dimensions.height.toInt())
    }

    private fun createFontTexture(text: String): BufferedImage {
        val dimensions: Rectangle2D = font.getStringBounds(text, FontRenderContext(AffineTransform(), true, true))

        val bufferedImage = BufferedImage(ceil(dimensions.width).toInt(), ceil(dimensions.height).toInt(), BufferedImage.TYPE_INT_ARGB)
        bufferedImage.createGraphics().also {
            it.font = font
            // Set the image background to transparent
            it.color = Color(255, 255, 255, 0)
            it.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
            // Char Render
            it.color = Color.white
            it.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            it.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            it.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            it.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            it.drawString(text, 0, it.fontMetrics.ascent)
            it.dispose()
        }

        return bufferedImage
    }

    private fun createTextureFromImage(image: BufferedImage): Int {
        val width = image.width
        val height = image.height
        val pixels = IntArray(width * height)

        image.getRGB(0, 0, width, height, pixels, 0, width)

        val buffer = BufferUtils.createByteBuffer(width * height * 4)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = pixels[y * width + x]
                buffer.put((pixel shr 16 and 0xFF).toByte()) // R
                buffer.put((pixel shr 8 and 0xFF).toByte())  // G
                buffer.put((pixel and 0xFF).toByte())         // B
                buffer.put(((pixel shr 24) and 0xFF).toByte()) // A
            }
        }
        buffer.flip()

        val textureID = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureID)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
            GL_RGBA, GL_UNSIGNED_BYTE, buffer
        )
        glBindTexture(GL_TEXTURE_2D, 0)

        buffer.clear()

        return textureID
    }

    private fun getCharWidth(c: Char): Float {
        val dimensions = font.getStringBounds(c.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )
        return dimensions.width.toFloat()
    }

    private fun getCharHeight(c: Char): Float {
        val dimensions = font.getStringBounds(c.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )
        return dimensions.height.toFloat()
    }

}
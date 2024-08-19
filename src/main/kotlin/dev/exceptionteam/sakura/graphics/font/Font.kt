package dev.exceptionteam.sakura.graphics.font

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.GraphicsEnvironment
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

class Font(
    val font: java.awt.Font,
    val size: Int
) {

    private val textureMap = mutableMapOf<Char, Int>() // Char & Texture id

    fun getTexture(c: Char): Int {
        textureMap[c]?.let { return it }
        val image = createFontTexture(c.toString(), size)
        val id = createTextureFromImage(image)
        textureMap[c] = id
        return id
    }

    fun canDisplay(c: Char): Boolean = font.canDisplay(c)

    fun getWidth(c: Char): Int = getCharWidth(c)
    fun getHeight(c: Char): Int = getCharHeight(c)

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

    private fun createFontTexture(text: String, fontSize: Int): BufferedImage {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val gc = gd.defaultConfiguration

        val (w, h) = getCharWidthHeight(text[0])
        val bufferedImage = gc.createCompatibleImage(w, h)
        val g2d = bufferedImage.createGraphics()

        g2d.font = font.deriveFont(fontSize)
        val fm = g2d.fontMetrics
        val rect = fm.getStringBounds(text, g2d)

        // 居中文本
        val x = (w - rect.width.toInt()) / 2
        val y = fm.ascent

        g2d.color = Color(255, 255, 255, 0)
        g2d.fillRect(0, 0, w, h)
        g2d.color = Color.white
        g2d.drawString(text, x, y)
        g2d.dispose()

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

        buffer.clear()

        return textureID
    }

    private fun getCharWidth(c: Char): Int {
        val dimensions = font.getStringBounds(c.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )
        return dimensions.width.toInt()
    }

    private fun getCharHeight(c: Char): Int {
        val dimensions = font.getStringBounds(c.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )
        return dimensions.height.toInt()
    }

}
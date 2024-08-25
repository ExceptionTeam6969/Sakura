package dev.exceptionteam.sakura.graphics.font.glyphs

import dev.exceptionteam.sakura.utils.math.ceilToInt
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Glyph(
    font: Font,
    char: Char
) {

    val dimensions: Rectangle2D = font.getStringBounds(char.toString(), FontRenderContext(AffineTransform(), true, true))
    var textureId: Int = 0

    init {
        val width = dimensions.width.ceilToInt()
        val height = dimensions.height.ceilToInt()

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        // Get the Graphics2D object from the BufferedImage
        val g2d = bufferedImage.createGraphics()

        // Set rendering hints for better text quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        // Set background color and fill the image
        g2d.color = Color(0, 0, 0, 0)
        g2d.fillRect(0, 0, width, height)

        // Set text color and font
        g2d.color = Color.WHITE
        g2d.font = font

        g2d.drawString(char.toString(), 0, 0)

        // Dispose the graphics context
        g2d.dispose()

        textureId = loadTexture(bufferedImage)
    }

    fun destroy() {
        if (textureId != 0) {
            glDeleteTextures(textureId)
        }
    }

    private fun loadTexture(image: BufferedImage): Int {
        // Extract pixel data from BufferedImage
        val pixels = IntArray(image.width * image.height)
        pixels.fill(0xFF0000FF.toInt())
//        image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)

        // Create a ByteBuffer to hold the pixel data
        val buffer = ByteBuffer.allocateDirect(4 * image.width * image.height)
        buffer.order(ByteOrder.nativeOrder())

        // Pack pixel data into the ByteBuffer in RGBA format
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val pixel = pixels[y * image.width + x]
                buffer.put(((pixel shr 16) and 0xFF).toByte())  // Red component
                buffer.put(((pixel shr 8) and 0xFF).toByte())   // Green component
                buffer.put((pixel and 0xFF).toByte())           // Blue component
                buffer.put(((pixel shr 24) and 0xFF).toByte())  // Alpha component
            }
        }

        buffer.flip() // Prepare buffer for reading

        // Generate a new texture ID
        val textureID = glGenTextures()

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureID)

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0,
            GL_RGBA, GL_UNSIGNED_BYTE, buffer)

        // Generate mipmaps (optional)
        glGenerateMipmap(GL_TEXTURE_2D)

        return textureID // Return the texture ID
    }

}
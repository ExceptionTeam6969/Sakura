package dev.exceptionteam.sakura.graphics.font.glyphs

import dev.exceptionteam.sakura.graphics.texture.ImageUtils
import dev.exceptionteam.sakura.utils.math.ceilToInt
import dev.luna5ama.kmogus.Arr
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.nio.ByteBuffer

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
        val g2d = bufferedImage.createGraphics()

        g2d.font = font

        g2d.color = Color(255, 255, 255, 0)
        g2d.fillRect(0, 0, bufferedImage.width, bufferedImage.height)

        g2d.color = Color.white
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.drawString(char.toString(), 0, g2d.fontMetrics.ascent)
        g2d.dispose()

        loadTexture(bufferedImage)
    }

    fun destroy() {
        if (textureId != 0) {
            glDeleteTextures(textureId)
        }
    }

    private fun loadTexture(image: BufferedImage) {
        textureId = glGenTextures()

        glBindTexture(GL_TEXTURE_2D, textureId)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        ImageUtils.uploadImage(image, GL_RGBA8, image.width, image.height)
    }

}
package dev.exceptionteam.sakura.graphics.font.glyphs

import dev.exceptionteam.sakura.graphics.texture.ImageUtils
import dev.exceptionteam.sakura.graphics.texture.Texture
import dev.exceptionteam.sakura.utils.math.ceilToInt
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

class Glyph(
    font: Font,
    char: Char
) {

    private val dimensions: Rectangle2D =
        font.getStringBounds(
            char.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )

    val width = dimensions.width.toFloat()
    val height = dimensions.height.toFloat()

    var texture: Texture

    init {
        val image = BufferedImage(width.ceilToInt(), height.ceilToInt(), BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()

        g2d.font = font

        g2d.background = Color(0, 0, 0, 0)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        g2d.color = Color.WHITE
        g2d.drawString(char.toString(), 0, g2d.fontMetrics.ascent)
        g2d.dispose()

        texture = Texture()
        texture.use {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

            ImageUtils.uploadImage(image)
        }
    }

    fun destroy() {
        texture.delete()
    }

}

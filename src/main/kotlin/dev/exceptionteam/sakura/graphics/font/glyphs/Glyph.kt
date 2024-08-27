package dev.exceptionteam.sakura.graphics.font.glyphs

import dev.exceptionteam.sakura.graphics.texture.ImageUtils
import dev.exceptionteam.sakura.utils.math.ceilToInt
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class Glyph(
    font: Font,
    char: Char
) {

    private val texture = Identifier("sakura", "textures/font/${char.toString().lowercase()}.png")

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
//        val mc = MinecraftClient.getInstance()
//
//        val bytes = ByteArrayOutputStream().use {
//            ImageIO.write(image, "png", it)
//            it.toByteArray()
//        }
//        val data = BufferUtils.createByteBuffer(bytes.size).put(bytes).also { it.flip() }
//        val tex = NativeImageBackedTexture(NativeImage.read(data))
//        mc.execute { mc.textureManager.registerTexture(texture, tex) }
//
//        textureId = mc.textureManager.getTexture(texture).glId

        textureId = glGenTextures()

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId)

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        ImageUtils.uploadImage(image, GL_RGBA, image.width, image.height)
    }

}
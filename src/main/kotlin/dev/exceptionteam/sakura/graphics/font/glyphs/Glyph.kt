package dev.exceptionteam.sakura.graphics.font.glyphs

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.utils.math.ceilToInt
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import org.lwjgl.BufferUtils
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.Locale
import javax.imageio.ImageIO

class Glyph(
    font: Font,
    char: Char
) {

    val imageTex = Identifier("sakura", "${Sakura.DIRECTORY}/glyphs/${font.name.lowercase(Locale.getDefault()).hashCode()}-${char.code}")

    private val dimensions: Rectangle2D =
        font.getStringBounds(
            char.toString(),
            FontRenderContext(AffineTransform(), true, true)
        )

    val width = dimensions.width.toFloat()
    val height = dimensions.height.toFloat()

//    var texture: Texture

    init {
        val image = BufferedImage(width.ceilToInt(), height.ceilToInt(), BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()

        g2d.font = font

        g2d.background = Color(255, 255, 255, 0)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        g2d.color = Color(255, 255, 255, 255)
        g2d.drawString(char.toString(), 0, g2d.fontMetrics.ascent)
        g2d.dispose()

//        texture = ImageUtils.uploadImageToTexture(image, GL45.GL_RGBA8)
        registerBufferedImageTexture(imageTex, image)
    }

    fun destroy() {
//        texture.delete()
    }

    companion object {
        private val mc = MinecraftClient.getInstance()
        fun registerBufferedImageTexture(identifier: Identifier, image: BufferedImage) {
            try {
                val bytes = ByteArrayOutputStream().use {
                    ImageIO.write(image, "png", it)
                    it.toByteArray()
                }
                registerTexture(identifier, bytes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun registerTexture(identifier: Identifier, bytes: ByteArray) {
            try {
                val data = BufferUtils.createByteBuffer(bytes.size).put(bytes).also { it.flip() }
                val tex = NativeImageBackedTexture(NativeImage.read(data))
                mc.execute { mc.textureManager.registerTexture(identifier, tex) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
